# Task Management API — REST Kontraktı (Dərs 3)

Bu sənəd API-nin **dizaynıdır**. Kod implementasiyası Dərs 4-dən başlayır.
Rəsmi maşın-oxunaqlı versiya: [`../openapi.yaml`](../openapi.yaml).

## Base URL
```
http://localhost:8080
```

## Endpoint-lər

| # | Method | Endpoint | Təsvir | Uğur statusu |
|---|--------|----------|--------|--------------|
| 1 | POST   | `/users` | User yarat | `201 Created` |
| 2 | GET    | `/users/{id}` | User-i al | `200 OK` |
| 3 | GET    | `/users/{id}/tasks` | User-in task-ları | `200 OK` |
| 4 | POST   | `/tasks` | Task yarat | `201 Created` |
| 5 | GET    | `/tasks` | Bütün task-lar (`?status=` filtri) | `200 OK` |
| 6 | GET    | `/tasks/{id}` | Task-ı al | `200 OK` |
| 7 | PATCH  | `/tasks/{id}` | Task-ı qismən yenilə | `200 OK` |
| 8 | DELETE | `/tasks/{id}` | Task-ı sil | `204 No Content` |

## HTTP status kodları (bu API-də)

| Kod | Nə vaxt |
|-----|---------|
| `200 OK` | Uğurlu GET / PATCH |
| `201 Created` | Yeni resurs yaradıldı (POST) |
| `204 No Content` | Uğurlu DELETE — body qaytarılmır |
| `400 Bad Request` | Yanlış/natamam input (validation) |
| `404 Not Found` | Resurs tapılmadı |
| `409 Conflict` | Dublikat (məs. email təkrarı) |
| `500 Internal Server Error` | Gözlənilməz server xətası |

## REST prinsipləri
- **Resource-oriented:** URI resursu göstərir (`/tasks/1`), feili yox.
- **HTTP metodu əməliyyatı bildirir:** GET (oxu), POST (yarat), PATCH (qismən dəyiş), DELETE (sil).
- **Stateless:** hər request özü-özünə tamdır.
- **Idempotency:** GET, PUT, DELETE idempotentdir; POST yox.

## Nümunə request/response-lar

### 1) POST /users
Request:
```json
{ "name": "Darya", "email": "darya@example.com" }
```
Response `201 Created`:
```json
{ "id": 1, "name": "Darya", "email": "darya@example.com" }
```
Xəta `409 Conflict` (email təkrarı):
```json
{
  "timestamp": "2026-09-04T10:00:00",
  "status": 409,
  "error": "Conflict",
  "message": "Bu email artıq mövcuddur: darya@example.com",
  "path": "/users"
}
```

### 4) POST /tasks
Request:
```json
{ "title": "Backend syllabus hazırla", "description": "8 dərslik plan", "priority": "HIGH", "userId": 1 }
```
Response `201 Created`:
```json
{
  "id": 1,
  "title": "Backend syllabus hazırla",
  "description": "8 dərslik plan",
  "status": "TODO",
  "priority": "HIGH",
  "userId": 1,
  "createdAt": "2026-09-04T10:00:00",
  "updatedAt": "2026-09-04T10:00:00"
}
```

### 7) PATCH /tasks/1
Request (yalnız status dəyişir):
```json
{ "status": "IN_PROGRESS" }
```
Response `200 OK`:
```json
{ "id": 1, "title": "Backend syllabus hazırla", "status": "IN_PROGRESS", "priority": "HIGH", "userId": 1 }
```

### 6) GET /tasks/99 — tapılmadı
Response `404 Not Found`:
```json
{
  "timestamp": "2026-09-04T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Task tapılmadı: id=99",
  "path": "/tasks/99"
}
```
## Category endpoint-ləri

### 1. POST /categories — yeni kateqoriya yarat

Request:

```http
POST http://localhost:8080/categories
Content-Type: application/json
```

```json
{
  "name": "Java",
  "description": "Java backend mövzuları"
}
```

Uğurlu response: `201 Created`

```json
{
  "id": 1,
  "name": "Java",
  "description": "Java backend mövzuları"
}
```

### 2. GET /categories — bütün kateqoriyaları al

Request:

```http
GET http://localhost:8080/categories
```

Uğurlu response: `200 OK`

```json
[
  {
    "id": 1,
    "name": "Java",
    "description": "Java backend mövzuları"
  },
  {
    "id": 2,
    "name": "Database",
    "description": "SQL və verilənlər bazaları"
  }
]
```

### 3. GET /categories/1 — ID ilə kateqoriya al

Request:

```http
GET http://localhost:8080/categories/1
```

Uğurlu response: `200 OK`

```json
{
  "id": 1,
  "name": "Java",
  "description": "Java backend mövzuları"
}
```

Kateqoriya tapılmadıqda: `404 Not Found`.

### 4. PUT /categories/1 — kateqoriyanı tam yenilə

Request:

```http
PUT http://localhost:8080/categories/1
Content-Type: application/json
```

```json
{
  "name": "Spring Boot",
  "description": "Spring Boot backend mövzuları"
}
```

Uğurlu response: `200 OK`

```json
{
  "id": 1,
  "name": "Spring Boot",
  "description": "Spring Boot backend mövzuları"
}
```

### 5. DELETE /categories/1 — kateqoriyanı sil

Request:

```http
DELETE http://localhost:8080/categories/1
```

Uğurlu response: `204 No Content`

Bu status kodunda response body qaytarılmır.

