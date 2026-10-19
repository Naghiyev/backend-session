-- Çalışma 1: HIGH prioritetli və TO-DO statuslu bütün task-lar
SELECT * FROM tasks WHERE priority = 'HIGH' AND status = 'TODO';

-- Çalışma 2: Heç bir task-ı olmayan user-lər (LEFT JOIN + IS NULL)
SELECT u.name FROM users u LEFT JOIN tasks t ON t.user_id = u.id WHERE t.id IS NULL;

-- Çalışma 3: Hər status üzrə task sayı (GROUP BY)
SELECT status, COUNT(*) AS task_count FROM tasks GROUP BY status;