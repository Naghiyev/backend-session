# Reliability basics (Dərs 7)

Backend production-da xarici sistemlərlə (database, digər API, message broker)
işləyir. Bu asılılıqlar bəzən yavaş və ya əlçatmaz olur. Reliability — sistemin
belə hallarda özünü necə aparmasıdır.

## Əsas anlayışlar
- **Timeout** — cavab müəyyən vaxtda gəlmirsə, gözləməyi dayandır (thread-ləri
  bloklamamaq üçün). Məs. HTTP client / DB query timeout.
- **Retry** — keçici (transient) xəta zamanı əməliyyatı məhdud sayda təkrarla.
  Diqqət: yalnız **idempotent** əməliyyatlar üçün və "backoff" ilə.
- **Failure handling** — xətanı udmaq yox, aydın idarə etmək (fallback, error response).
- **External service dependency** — kənar servis çökəndə bütün sistem çökməməlidir.
- **Graceful degradation** — funksionallığın bir hissəsi işləməsə belə, sistem
  qismən xidmət göstərməyə davam edir (məs. cache-dən köhnə məlumat).

## Bu layihədə praktik nümunələr
- **Timeout:** `application.properties`-də DB connection/query timeout təyin etmək olar.
- **Retry:** `spring-retry` ilə `@Retryable` — keçici xətalarda avtomatik təkrar.
- **Testlər:** `TaskControllerTest` göstərir ki, xəta halında API 4xx/5xx qaytarır
  (çökmür) — bu, reliability-nin əsas tələbidir.

## Testing ilə əlaqə
Reliability yalnız kodda deyil, **testlərdə** də yoxlanır: negative və exception
ssenariləri (`createTask_userNotFound_throws`, `getById_missing_returns404`)
sistemin xətaya necə reaksiya verdiyini təsdiqləyir.
