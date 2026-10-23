# Transactional Outbox

Bu layihədə `POST /tasks` və status dəyişən `PATCH /tasks/{id}` sorğuları Kafka-ya event göndərir.
Event-lər **birbaşa** Kafka-ya yox, əvvəlcə eyni DB tranzaksiyasında `outbox_event` cədvəlinə yazılır.

## Problem (dual write)

```java
taskRepository.save(task);          // 1. PostgreSQL
kafkaTemplate.send("task.events"); // 2. Kafka  ← bu iki yazı arasında ümumi tranzaksiya yoxdur
```

- Kafka işləmir → task yaranır, event itir.
- Event gedir, sonra DB commit uğursuz olur → mövcud olmayan task haqqında event (fantom event).

## Həll

```
POST /tasks ──► TaskServiceImpl (@Transactional)
                  ├─ INSERT tasks
                  └─ INSERT outbox_event (PENDING)   ← eyni commit
                                 │
OutboxRelay (@Scheduled, 1 san)  ▼
  SELECT ... FOR UPDATE SKIP LOCKED → Kafka "task.events" (key = taskId) → SENT
                                 │
TaskEventsListener (@KafkaListener)
  INSERT processed_message (ON CONFLICT DO NOTHING)  ← dublikat yoxlaması
  INSERT task_notification                           ← eyni tranzaksiya
```

## Paket strukturu

| Fayl | Rol |
|---|---|
| `outbox/OutboxEvent` | Outbox cədvəlinin entity-si (PENDING / SENT / FAILED) |
| `outbox/OutboxWriter` | `Propagation.MANDATORY` — yalnız açıq tranzaksiya daxilində yazır |
| `outbox/OutboxRelay` | Polling publisher: kilidlə → göndər → ack gözlə → SENT; xətada backoff |
| `outbox/OutboxCleanupJob` | Hər gecə 03:15 köhnə SENT sətirləri silir |
| `outbox/KafkaConfig` | `task.events` topic-i (3 partition), consumer error handler |
| `outbox/OutboxController` | Demo üçün: `/outbox/events`, `/outbox/stats`, `/outbox/notifications` |
| `outbox/event/*` | `TaskCreatedEvent`, `TaskStatusChangedEvent` |
| `outbox/consumer/*` | İdempotent consumer, `processed_message`, `task_notification` |

## İşə salmaq

```bash
docker compose up -d postgres kafka kafka-ui
mvn spring-boot:run
```

Sonra `docs/requests.http` faylındakı 15–21 sorğularını göndərin. Kafka UI: http://localhost:8090

## Dərsdə göstəriləcək eksperimentlər

1. **Kafka dayananda nə olur?** `docker stop task-kafka` → task yaradın → API yenə `201` qaytarır.
   `/outbox/events?status=PENDING` sətrin `attempts` və `lastError` sahələrinin artdığını göstərir.
   `docker start task-kafka` → event avtomatik göndərilir, status `SENT` olur.
2. **Atomiklik:** mövcud olmayan `userId` ilə task yaradın → `404`, outbox-da da sətir yoxdur.
3. **Dublikat:** relay at-least-once işləyir. Eyni eventi Kafka UI-dən (eyni `eventId` header-i ilə)
   yenidən göndərin → logda `Dublikat event ötürüldü`, `/outbox/notifications`-da ikinci bildiriş yoxdur.
4. **Sıra:** key = taskId, ona görə eyni task-ın `TaskCreated` və `TaskStatusChanged` eventləri
   eyni partition-a düşür və ardıcıl oxunur.

## Qeydlər

- `spring.jpa.hibernate.ddl-auto=update` cədvəlləri avtomatik yaradır. Production-da Flyway/Liquibase
  istifadə edin və `CREATE INDEX ... WHERE status = 'PENDING'` kimi partial index əlavə edin.
- Payload-a həssas məlumat (şifrə, kart nömrəsi, FIN) qoymayın: outbox və Kafka topic-ləri uzun saxlanılır.
- Alternativ relay: Debezium CDC (WAL-dan oxuyur, polling lazım olmur).
