-- Task Management API — relational schema (Lesson 7)
-- İşə salmaq:  psql -h localhost -U taskuser -d taskdb -f db/schema.sql

CREATE TABLE IF NOT EXISTS users (
    id    BIGSERIAL PRIMARY KEY,
    name  VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE           -- UNIQUE constraint
);

CREATE TABLE IF NOT EXISTS categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50)  NOT NULL UNIQUE,
    description VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS tasks (
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(200) NOT NULL,
    description VARCHAR(1000),
    status      VARCHAR(20)  NOT NULL,           -- TODO | IN_PROGRESS | DONE
    priority    VARCHAR(20)  NOT NULL,           -- LOW | MEDIUM | HIGH
    user_id     BIGINT NOT NULL REFERENCES users(id),  -- FOREIGN KEY
    category_id BIGINT REFERENCES categories(id) ON DELETE SET NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT now(),
    updated_at  TIMESTAMP NOT NULL DEFAULT now()
);

ALTER TABLE tasks
    ADD COLUMN IF NOT EXISTS category_id BIGINT REFERENCES categories(id) ON DELETE SET NULL;

-- Index: user üzrə task axtarışını sürətləndirir
CREATE INDEX IF NOT EXISTS idx_tasks_user_id ON tasks(user_id);
CREATE INDEX IF NOT EXISTS idx_tasks_status  ON tasks(status);
CREATE INDEX IF NOT EXISTS idx_tasks_category_id ON tasks(category_id);
