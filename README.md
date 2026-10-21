# Task Management API — Backend Development Fundamentals with Java

> **"Backend Development Fundamentals with Java"** təliminin praktik layihəsi.
> Vahid layihə — *Task Management API* — mərhələli şəkildə, **dərsdən-dərsə (12 dərs)** inkişaf etdirilir.

**Təlimçi:** Darya Naghiyev

---

## 🌿 Branch xəritəsi

| Branch | Dərs | Mövzu | Layihənin vəziyyəti |
|--------|------|-------|---------------------|
| `branch_lesson1` | 1 | Java Essentials | Plain Java: in-memory model, repository və service |
| `branch_lesson2` | 2 | Layered Architecture & Clean Code | Paketlər, DTO, mapper, custom exception (concrete qatlar) |
| `branch_lesson3` | 3 | SOLID & Dependency Injection | Interface-based design, constructor DI, dəyişdirilə bilən implementasiya |
| `branch_lesson4` **←** | 4 | HTTP, REST & API Design | OpenAPI kontrakt, Postman, status kodları |
| `branch_lesson5` | 5 | Spring Boot Fundamentals | Spring Boot, IoC/DI, ilk REST endpoint-lər |
| `branch_lesson6` | 6 | Full REST API with Spring Boot | Bütün endpoint-lər, ResponseEntity (in-memory) |
| `branch_lesson7` | 7 | Relational Databases & SQL | PostgreSQL, docker-compose, schema, SQL |
| `branch_lesson8` | 8 | JPA & Hibernate Persistence | Entity, JpaRepository, əlaqələr, transaction |
| `branch_lesson9` | 9 | Validation | Bean Validation, @Valid, server-side yoxlama |
| `branch_lesson10` | 10 | Exception Handling & Logging | Global handler, standart ApiError, logging |
| `branch_lesson11` | 11 | Backend Testing & Reliability | JUnit 5, Mockito, MockMvc |
| `branch_lesson12` | 12 | Production Backend | JWT security, caching, Docker, CI/CD — yekun |
| `main` | — | Yekun | 12-ci dərsin tam versiyası |

> Hər branch əvvəlkinin üzərinə qurulur (**cumulative**). Yəni `branch_lesson8`
> özündə 1–8 dərslərinin bütün işini saxlayır.

```bash
git clone https://github.com/Naghiyev/backend-session.git
cd backend-session
git checkout branch_lesson4
```

---

## 📍 Bu branch: Lesson 4 — HTTP, REST & API Design

Dərs 4-ün ilkin versiyasında API kontraktı dizayn olunur. Bu branch-da əlavə olaraq Category HTTP API implementasiya edilib. `openapi.yaml`, `docs/API.md`, Postman
kolleksiyası və `.http` faylı əlavə edildi.

- `openapi.yaml` — OpenAPI 3.0 spesifikasiyası
- `docs/API.md`, `docs/postman_collection.json`, `docs/requests.http`
### İşə salmaq
```bash
mvn -q compile exec:java
```

Dərs qeydləri: [`LESSON.md`](LESSON.md) · Ev tapşırığı: [`HOMEWORK.md`](HOMEWORK.md)

## Category HTTP API

Category CRUD Spring Boot ilə işləyir: `mvn spring-boot:run`.
Base URL: http://localhost:8080/categories. Məlumatlar yaddaşda saxlanılır.
User/Task HTTP endpoint-ləri hələ implementasiya olunmayıb.
Testlər: `mvn test`. Konsol demosu: `mvn compile exec:java`.
