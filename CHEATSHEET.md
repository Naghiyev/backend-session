# Dərs Bələdçisi — branch diff, demo & homework

> Hər dərsdən əvvəl həmin branch-a keç, amma **diff-i əvvəlki branch-dan** göstər — beləcə iştirakçılar “layihəyə bu dərs nə əlavə olundu”-nu görürlər.

**Diff-ə necə baxım**

- **Terminal:** `git show branch_lessonN` — həmin dərsin dəyişikliyi (hər branch = 1 commit)
- **İki dərs arası:** `git diff branch_lessonA branch_lessonB --stat`
- **IntelliJ:** Git → Log → commit-ə klik (yan-yana diff)
- **GitHub:** aşağıdakı *Compare* linkləri
- **Bütün mənzərə:** `git log --oneline --graph main`

> Mapping: **Dərs 2 → `branch_lesson1` … Dərs 13 → `branch_lesson12`** · Dərs 1 giriş dərsidir (branch yoxdur).

---

## Dərs 1 — Course Kickoff & Backend Mindset

- **Branch:** — (giriş dərsi, kod yoxdur)
- **Danışıq nöqtələri:**
  - Mental model: client → controller → service → repository → database
  - Client–server, request–response lifecycle, stateless
  - Layihə ilə tanışlıq: Task Management API (User + Task)
  - Təlim necə qurulub: hər dərs bir git branch
- **Demo (canlı):** Kod yox — bütün laptop-larda mühiti işə sal (JDK, IntelliJ, Git, Docker) və reponu klonlat.
- **Homework:** Mühiti qur, reponu klonla, `branch_lesson1`-i işə sal.

## Dərs 2 — Java Essentials for Backend

- **Branch:** `branch_lesson1`
- **Diff:** `git show branch_lesson1`
- **İlk commit:** https://github.com/Naghiyev/backend-session/commits/branch_lesson1
- **Danışıq nöqtələri:**
  - JDK / JRE / JVM; `javac` → bytecode → JVM
  - OOP: class, encapsulation, interface
  - Collections, exception, enum (`TaskStatus`, `Priority`)
  - In-memory repository & service — hələ framework yoxdur
- **Demo (canlı):** Boş `title` ilə task yaratmağa çalış → `IllegalArgumentException`.
- **Homework:** Java domain-ə `Category` model əlavə et.

## Dərs 3 — Application Architecture & Clean Code

- **Branch:** `branch_lesson2`
- **Diff:** `git show branch_lesson2`
- **Compare:** https://github.com/Naghiyev/backend-session/compare/branch_lesson1...branch_lesson2
- **Danışıq nöqtələri:**
  - Separation of Concerns; Controller → Service → Repository
  - DTO vs Entity; mapper
  - Package strukturu, adlandırma
- **Demo (canlı):** Business logic-i controller-ə yazmağın niyə pis olduğunu göstər, sonra service-ə köçür.
- **Homework:** `Category` üçün tam layered qat dəsti.

## Dərs 4 — SOLID & Dependency Injection

- **Branch:** `branch_lesson3`
- **Diff:** `git show branch_lesson3`
- **Compare:** https://github.com/Naghiyev/backend-session/compare/branch_lesson2...branch_lesson3
- **Danışıq nöqtələri:**
  - Interface + implementation; constructor injection
  - DIP: service abstraction-dan asılıdır
  - Tight vs loose coupling
- **Demo (canlı):** Repository implementasiyasını dəyiş (in-memory → başqa) — service kodu dəyişmir.
- **Homework:** İkinci repository implementasiyası / decorator (SOLID).

## Dərs 5 — HTTP, REST & API Design

- **Branch:** `branch_lesson4`
- **Diff:** `git show branch_lesson4`
- **Compare:** https://github.com/Naghiyev/backend-session/compare/branch_lesson3...branch_lesson4
- **Danışıq nöqtələri:**
  - HTTP method-lar, status kodları (200/201/204/400/404/409)
  - Path vs query param; JSON serialization
  - REST, idempotency, API contract (OpenAPI)
- **Demo (canlı):** `openapi.yaml`-ı Swagger Editor-də aç; Postman ilə request göndər.
- **Homework:** OpenAPI-yə `Category` endpoint-ləri.

## Dərs 6 — Spring Boot Fundamentals

- **Branch:** `branch_lesson5`
- **Diff:** `git show branch_lesson5`
- **Compare:** https://github.com/Naghiyev/backend-session/compare/branch_lesson4...branch_lesson5
- **Danışıq nöqtələri:**
  - IoC/DI — “Spring Dərs 4-də əl ilə etdiyimizi avtomatik edir”
  - `@RestController` / `@Service` / `@Repository`
  - Starters, `application.properties`, embedded server
- **Demo (canlı):** Constructor dependency-ni sil → Spring bean xətası; geri qaytar.
- **Homework:** `GET /users` və sadə axtarış (Spring Boot).

## Dərs 7 — Full REST API with Spring Boot

- **Branch:** `branch_lesson6`
- **Diff:** `git show branch_lesson6`
- **Compare:** https://github.com/Naghiyev/backend-session/compare/branch_lesson5...branch_lesson6
- **Danışıq nöqtələri:**
  - Bütün endpoint-lər; `@RequestParam` ilə filter
  - ResponseEntity: 201 Created, 204 No Content
  - PATCH ilə qismən yeniləmə
- **Demo (canlı):** create-də əvvəl 200 qaytar → sonra 201-ə düzəlt; fərqi izah et.
- **Homework:** PUT vs PATCH; `priority` filtri.

## Dərs 8 — Relational Databases & SQL

- **Branch:** `branch_lesson7`
- **Diff:** `git show branch_lesson7`
- **Compare:** https://github.com/Naghiyev/backend-session/compare/branch_lesson6...branch_lesson7
- **Danışıq nöqtələri:**
  - Table, PK/FK, əlaqələr (1–M)
  - SELECT/INSERT/UPDATE/DELETE, JOIN, GROUP BY
  - docker-compose ilə PostgreSQL; index
- **Demo (canlı):** `docker compose up -d`; `psql` ilə `schema.sql` işə sal; FK pozuntusunu göstər.
- **Homework:** SQL: JOIN və GROUP BY çalışmaları.

## Dərs 9 — Persistence with JPA & Hibernate

- **Branch:** `branch_lesson8`
- **Diff:** `git show branch_lesson8`
- **Compare:** https://github.com/Naghiyev/backend-session/compare/branch_lesson7...branch_lesson8
- **Danışıq nöqtələri:**
  - `@Entity`, `@Id`, `@GeneratedValue`; `@ManyToOne`/`@OneToMany`
  - `JpaRepository` + derived queries
  - `@Transactional`; `ddl-auto`
- **Demo (canlı):** `show-sql=true` — Hibernate-in yaratdığı SQL-i log-da göstər.
- **Homework:** `Category` entity + derived query (JPA).

## Dərs 10 — Validation

- **Branch:** `branch_lesson9`
- **Diff:** `git show branch_lesson9`
- **Compare:** https://github.com/Naghiyev/backend-session/compare/branch_lesson8...branch_lesson9
- **Danışıq nöqtələri:**
  - Server-side validation; `@NotBlank`/`@Email`/`@NotNull`/`@Size`
  - `@Valid` controller-də
  - Yanlış input → 400 Bad Request
- **Demo (canlı):** `@Valid`-i sil → yanlış data keçir; geri qaytar.
- **Homework:** Əlavə validation qaydaları.

## Dərs 11 — Exception Handling & Logging

- **Branch:** `branch_lesson10`
- **Diff:** `git show branch_lesson10`
- **Compare:** https://github.com/Naghiyev/backend-session/compare/branch_lesson9...branch_lesson10
- **Danışıq nöqtələri:**
  - `@RestControllerAdvice`; standart `ApiError` model
  - 400/404/409/500 mapping
  - Logging səviyyələri (INFO/WARN/ERROR)
- **Demo (canlı):** Əvvəl: 500 stacktrace. Sonra: təmiz 404 + `ApiError` body.
- **Homework:** Yeni custom exception + handler.

## Dərs 12 — Backend Testing & Reliability

- **Branch:** `branch_lesson11`
- **Diff:** `git show branch_lesson11`
- **Compare:** https://github.com/Naghiyev/backend-session/compare/branch_lesson10...branch_lesson11
- **Danışıq nöqtələri:**
  - JUnit 5; Mockito `@Mock`/`@InjectMocks`/`when`/`verify`
  - MockMvc ilə controller testi
  - Arrange – Act – Assert
- **Demo (canlı):** Kodu qəsdən poz → test qırmızı olur; düzəlt → yaşıl.
- **Homework:** `UserControllerTest` + parameterized test.

## Dərs 13 — Production Backend & Capstone

- **Branch:** `branch_lesson12`
- **Diff:** `git show branch_lesson12`
- **Compare:** https://github.com/Naghiyev/backend-session/compare/branch_lesson11...branch_lesson12
- **Danışıq nöqtələri:**
  - JWT, Bearer; authentication vs authorization
  - 401 vs 403; role-based
  - Caching, Docker, CI/CD
- **Demo (canlı):** Token-siz request → 401; ADMIN olmayan `DELETE` → 403.
- **Homework:** Final: `Category` uçtan-uca + BCrypt + Docker.
