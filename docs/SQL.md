# SQL — Task Management API (Lesson 7)

Bu dərsdə application hələ **in-memory**-dir; məqsəd relational database və SQL ilə tanışlıqdır.
Sxem: [`../db/schema.sql`](../db/schema.sql).

## Bazanı qaldır
```bash
docker compose up -d postgres
psql -h localhost -U taskuser -d taskdb -f db/schema.sql   # parol: taskpass
```

## Nümunə sorğular

```sql
-- INSERT
INSERT INTO users (name, email) VALUES ('Darya', 'darya@example.com');
INSERT INTO tasks (title, status, priority, user_id)
VALUES ('Backend syllabus', 'TODO', 'HIGH', 1);

-- SELECT + WHERE
SELECT * FROM tasks WHERE user_id = 1 AND status = 'TODO';

-- ORDER BY
SELECT id, title, priority FROM tasks ORDER BY created_at DESC;

-- JOIN: task + sahibinin adı
SELECT t.id, t.title, u.name AS owner
FROM tasks t
JOIN users u ON u.id = t.user_id;

-- GROUP BY: hər user-in task sayı
SELECT u.name, COUNT(t.id) AS task_count
FROM users u
LEFT JOIN tasks t ON t.user_id = u.id
GROUP BY u.name;

-- UPDATE / DELETE
UPDATE tasks SET status = 'IN_PROGRESS' WHERE id = 1;
DELETE FROM tasks WHERE id = 1;
```

## Çalışmalar
1. `HIGH` prioritetli və `TODO` statuslu bütün task-ları tap.
2. Heç bir task-ı olmayan user-ləri tap (LEFT JOIN + IS NULL).
3. Hər status üzrə task sayını çıxar (GROUP BY).

> Növbəti dərsdə (Lesson 8) bu SQL işini **JPA/Hibernate** avtomatik edəcək.
