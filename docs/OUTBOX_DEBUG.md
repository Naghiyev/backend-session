# Outbox-u debug edərək anlamaq: addım-addım plan

Bu plan bir task-ın bütün yolunu izləyir: HTTP sorğusu → DB tranzaksiyası → outbox cədvəli →
relay → Kafka → consumer → bildiriş. Hər mərhələdə **harada dayanmalı**, **nəyə baxmalı** və
**nəyi görməlisiniz** yazılıb. Mərhələləri ardıcıl keçin: hər biri əvvəlkinin üzərində qurulur.

Sətir nömrələri `feature/outbox-pattern` branch-ının ilk commit-inə aiddir. Kod dəyişsə,
metod adı və kod parçası ilə tapın.

---

## 0. Hazırlıq (bir dəfə)

```bash
git checkout feature/outbox-pattern
docker compose up -d postgres kafka kafka-ui
```

**psql-i ayrıca terminalda açıq saxlayın**, hər mərhələdə ona baxacağıq:

```bash
docker exec -it taskdb-postgres psql -U taskuser -d taskdb
```

```sql
-- Bu sorğunu tez-tez işlədəcəyik (\e ilə yadda saxlamaq olar)
SELECT id, event_type, aggregate_id, status, attempts, next_attempt_at, last_error
  FROM outbox_event ORDER BY created_at DESC LIMIT 10;
```

**IntelliJ ayarları** (vacibdir, yoxsa debug qarışıq olur):

1. Run/Debug Configuration → *Program arguments*: `--outbox.poll-interval=600000`
   Relay 10 dəqiqədə bir işləyəcək. Belə olanda 1-ci və 2-ci mərhələdə eventlər `PENDING` qalır və
   onlara rahat baxa bilirsiniz. 3-cü mərhələdə bu arqumenti silin.
2. Hər breakpoint-də sağ klik → **Suspend: Thread** (All yox).
   Səbəb: relay `@Scheduled` thread-də, HTTP sorğusu Tomcat thread-də, consumer isə Kafka thread-də
   işləyir. "All" seçilsə, bir breakpoint bütün tətbiqi dondurur və nə baş verdiyini görmək olmur.
3. Debug pəncərəsində **Threads** görünüşünü açın. Hansı thread-də olduğunuzu həmişə yoxlayın:
   `http-nio-...-exec-N` (HTTP), `scheduling-1` (relay), `org.springframework.kafka...` (consumer).

Token alın (`docs/requests.http` → 15-ci sorğu), sonra mərhələlərə keçin.

---

## 1. Atomiklik: task və event bir tranzaksiyada yazılır

**Sual:** Task yaradılanda outbox sətri nə vaxt görünür?

| Breakpoint | Yer |
|---|---|
| B1 | `TaskServiceImpl.createTask` → `outboxWriter.append(...)` (sətir 54) |
| B2 | `OutboxWriter.append` → `repository.save(...)` (sətir 31) |
| B3 | `TaskServiceImpl.createTask` → `return TaskMapper.toResponse(saved);` (sətir 58) |

**Addımlar:**

1. `POST /tasks` göndərin (16-cı sorğu). B1-də dayanır.
   - *Evaluate* (Alt+F8): `saved.getId()` → ID artıq var. Bu ID `IDENTITY` strategiyası ilə INSERT zamanı DB-dən gəlib.
   - psql: `SELECT * FROM tasks ORDER BY id DESC LIMIT 1;` → **yeni task görünmür.**
     Səbəb: tranzaksiya hələ commit olunmayıb. PostgreSQL `READ COMMITTED` səviyyəsində başqa sessiya
     commit olunmamış sətri görmür.
2. F9 → B2. *Evaluate*: `TransactionSynchronizationManager.isActualTransactionActive()` → `true`.
   Bu, `Propagation.MANDATORY`-nin şərtidir.
3. F9 → B3. psql-də outbox sorğusu → **hələ boşdur.**
4. F9 (davam). Cavab `201` gəlir. Psql-də indi **hər ikisi** birdən görünür: task və `PENDING` statuslu event.

**Nəticə:** iki INSERT bir commit-də görünür. Ya hər ikisi var, ya heç biri.

**Əlavə eksperiment: rollback**
- Mövcud olmayan `userId: 999` ilə sorğu göndərin → `404`. Outbox-da yeni sətir yoxdur, çünki exception `append`-ə çatmamış atıldı.
- Daha maraqlısı: B3-də dayananda Debug pəncərəsində frame-ə sağ klik → **Throw Exception** →
  `new RuntimeException("test")`. Cavab `500` olur. Psql-də **nə task, nə event var**,
  halbuki hər iki INSERT artıq icra olunmuşdu.

**MANDATORY-ni yoxlamaq:** `OutboxController`-ə müvəqqəti olaraq tranzaksiyasız
`outboxWriter.append(...)` çağırışı əlavə etsəniz, `IllegalTransactionStateException:
No existing transaction found for transaction marked with propagation 'mandatory'` alacaqsınız.

---

## 2. Status dəyişikliyi: yalnız həqiqi dəyişiklikdə event

| Breakpoint | Yer |
|---|---|
| B4 | `TaskServiceImpl.updateTask` → `TaskStatus oldStatus = task.getStatus();` (sətir 100) |
| B5 | `TaskServiceImpl.updateTask` → `outboxWriter.append(...)` (sətir 118) |

1. `PATCH /tasks/{id}` `{"status":"IN_PROGRESS"}` → B4. F8 ilə addımlayın, `oldStatus = TODO`.
2. B5-ə çatır, çünki `IN_PROGRESS != TODO`.
3. Eyni sorğunu **təkrar** göndərin. B5-ə **çatmır**: status artıq `IN_PROGRESS`-dir.
4. `{"title":"yeni ad"}` göndərin. B5-ə çatmır, çünki status dəyişməyib.

psql: indi `PENDING`-də 2 event var: `TaskCreated` və `TaskStatusChanged`. Hər ikisinin
`aggregate_id`-si eynidir (task ID). Bu sonra Kafka-da sıranı təmin edəcək.

---

## 3. Relay: outbox-dan Kafka-ya

`--outbox.poll-interval=600000` arqumentini **silin** və tətbiqi yenidən debug-da başladın.

| Breakpoint | Yer |
|---|---|
| R1 | `OutboxRelay.publishBatch` → `repository.lockNextBatch(...)` sətrindən **sonrakı** sətir (sətir 58) |
| R2 | `OutboxRelay.publishBatch` → `event.markSent();` (sətir 67) |

1. Tətbiq qalxan kimi R1-də dayanır (thread: `scheduling-1`). *Variables*: `batch` siyahısında 2 event.
2. **SKIP LOCKED-i öz gözünüzlə görün.** R1-də dayanmışkən psql-də:
   ```sql
   BEGIN;
   SELECT id, event_type FROM outbox_event
    WHERE status = 'PENDING' FOR UPDATE SKIP LOCKED;
   -- 0 sətir! Relay onları kilidləyib, ikinci "relay" onları ötürür.
   SELECT id, event_type FROM outbox_event WHERE status = 'PENDING';
   -- adi SELECT isə hər ikisini görür (kilid yalnız yazmanı bloklayır)
   ROLLBACK;
   ```
   Tətbiqin ikinci instance-ı olsaydı, o da məhz belə davranardı.
3. F9 → R2. `.get(...)` keçib, yəni Kafka mesajı qəbul edib təsdiqləyib (`acks=all`).
   - Kafka UI (http://localhost:8090) → Topics → `task.events` → Messages: mesaj **artıq oradadır**.
   - psql: status **hələ `PENDING`-dir**, çünki `markSent()` hələ commit olunmayıb.
4. **Bu, at-least-once-ın mənbəyidir.** Burada (R2-də) tətbiqi **Stop** (Ctrl+F2) ilə dayandırın.
   Mesaj Kafka-dadır, amma DB-də `PENDING` qalıb. Tətbiqi yenidən başladın: relay eyni eventi
   **ikinci dəfə** göndərir. Kafka UI-da eyni `eventId` header-li 2 mesaj görəcəksiniz.
   Bunu 5-ci mərhələdə consumer həll edəcək.
5. Breakpoint-ləri söndürün, F9. psql: hər iki event `SENT`, `published_at` dolu.

**Kafka UI-da mesaja baxın:**
- *Key* = task ID. Eyni task-ın eventləri eyni partition-dadır.
- *Headers*: `eventId`, `eventType`.
- *Value* = `payload` sütunundakı JSON.

---

## 4. Kafka dayananda nə olur? (retry və backoff)

| Breakpoint | Yer |
|---|---|
| R3 | `OutboxRelay.publishBatch` → `event.markFailedAttempt(...)` (sətir 75) |
| R4 | `OutboxRelay.publishBatch` → `break;` (sətir 86) |

1. `docker stop task-kafka`
2. `POST /tasks` göndərin → **`201`**. API Kafka-dan asılı deyil. Pattern-in əsas faydası budur.
3. ~5 saniyə sonra R3-də dayanır (`max.block.ms=5000` gözlənildi). *Variables*: `cause` →
   `TimeoutException` və ya oxşar xəta.
4. F8 ilə `markFailedAttempt` içinə girin (F7): `attempts=1`, `nextAttemptAt = now + 2 san`.
5. R4: `break`. Partiyanın qalan eventləri toxunulmamış qalır. Sonrakı eventlər bundan qabağa keçə bilməz.
6. Breakpoint-ləri söndürüb bir müddət gözləyin. psql-də `attempts` 1, 2, 3... artır,
   `next_attempt_at` arasındakı fasilə 2, 4, 8, 16 san... olur. `last_error` doludur.
7. `docker start task-kafka` → növbəti cəhddə event `SENT` olur. Heç nə itmədi.

**FAILED statusunu görmək üçün:** `--outbox.max-attempts=2` ilə başladın, Kafka-nı dayandırın,
task yaradın. 2 cəhddən sonra status `FAILED` olur, logda `ERROR ... manual baxış lazımdır` yazılır.
Bu event bir daha avtomatik göndərilmir.

---

## 5. Consumer: dublikatı necə ayırır?

| Breakpoint | Yer |
|---|---|
| C1 | `TaskEventsListener.onEvent` → `int inserted = jdbcTemplate.update(...)` (sətir 53) |
| C2 | `TaskEventsListener.onEvent` → `if (inserted == 0)` (sətir 59) |
| C3 | `TaskEventsListener.save` → `notificationRepository.save(...)` (sətir 80) |

1. Yeni task yaradın. C1-də dayanır (thread: `org.springframework.kafka.KafkaListenerEndpointContainer#0-0-C-1`).
   *Variables* → `record` → `partition`, `offset`, `key`. Header-lərdən `eventId`-ni qeyd edin.
2. F8 → C2: `inserted = 1`. İlk dəfədir, emal olunur → C3.
3. psql:
   ```sql
   SELECT * FROM processed_message ORDER BY processed_at DESC LIMIT 5;
   SELECT * FROM task_notification ORDER BY created_at DESC LIMIT 5;
   ```
   C3-də dayananda **ikisi də boşdur** (tranzaksiya açıqdır). F9-dan sonra ikisi birlikdə görünür.
4. **Dublikatı yaradın.** Kafka UI → `task.events` → *Produce Message*:
   - Key: həmin task ID
   - Headers: `{"eventId":"<qeyd etdiyiniz UUID>","eventType":"TaskCreated"}`
   - Value: Kafka UI-dakı orijinal mesajın value-su
   Göndərin → C2-də `inserted = 0` → `return`. Logda `Dublikat event ötürüldü`.
   `/outbox/notifications` → ikinci bildiriş **yoxdur**.
   (3-cü mərhələnin 4-cü addımında yaratdığınız dublikat da məhz belə ötürülüb. Logda yoxlayın.)

**Niyə `ON CONFLICT DO NOTHING`?** Əvvəl `SELECT`, sonra `INSERT` etsəydik, iki consumer
thread eyni anda `SELECT` edib ikisi də "yoxdur" görə bilərdi. Primary key + `ON CONFLICT`
bu yoxlamanı DB səviyyəsində atomik edir.

**Consumer xətası:** C3-də **Throw Exception** edin. `processed_message` sətri də rollback olur,
offset commit olunmur və `DefaultErrorHandler` mesajı 1 san fasilə ilə 3 dəfə yenidən verir
(C1 təkrar dayanır). 4-cü uğursuzluqdan sonra logda `Event emal olunmadı, ötürülür` yazılır.

---

## 6. Hər şeyi bir yerdə görmək

Breakpoint-ləri söndürün və `docs/requests.http`-də 16 → 17 → 18 → 20 → 21 sorğularını göndərin.

```sql
-- task-ın bütün yolu bir sorğuda
SELECT t.id AS task_id, o.event_type, o.status, o.attempts, o.published_at,
       (pm.event_id IS NOT NULL) AS consumed, n.message
  FROM tasks t
  JOIN outbox_event o       ON o.aggregate_id = t.id::text
  LEFT JOIN processed_message pm ON pm.event_id = o.id
  LEFT JOIN task_notification n  ON n.event_id = o.id
 ORDER BY o.created_at DESC LIMIT 10;
```

---

## Yoxlama siyahısı: anladım?

- [ ] Commit olunmamış sətri psql-də niyə görmürəm? (1-ci mərhələ)
- [ ] `Propagation.MANDATORY` nəyi qoruyur? (1)
- [ ] Status dəyişməyən PATCH-də niyə event yoxdur? (2)
- [ ] `SKIP LOCKED` iki relay-i necə ayırır? (3)
- [ ] Dublikat haradan yaranır və niyə bu normaldır? (3, addım 4)
- [ ] Kafka dayananda API niyə yenə 201 qaytarır? (4)
- [ ] Relay xətadan sonra niyə `break` edir? (4)
- [ ] Consumer dublikatı hansı sətirdə ayırır? (5)
- [ ] Dedup və bildiriş niyə eyni tranzaksiyadadır? (5)

## Tez-tez rast gəlinən çətinliklər

| Problem | Səbəb / həll |
|---|---|
| Breakpoint hər saniyə dayanır | Relay `@Scheduled` işləyir. `--outbox.poll-interval=600000` istifadə edin və ya R breakpoint-lərini söndürün |
| HTTP sorğusu timeout olur | Breakpoint-də çox qaldınız. Client timeout-u artırın və ya daha tez davam edin |
| Bir breakpoint bütün tətbiqi dondurur | Suspend policy `All`-dur, `Thread` edin |
| Consumer breakpoint-i işləmir | Kafka qalxmayıb və ya `group-id` artıq offset-i commit edib. Yeni task yaradın |
| Relay `Connection to node -1 could not be established` | Kafka konteyneri işləmir: `docker ps`, `docker compose up -d kafka` |
| psql-də cədvəl yoxdur | Tətbiq bir dəfə başlamalıdır, `ddl-auto=update` cədvəlləri yaradır |
