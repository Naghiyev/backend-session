
-- Task Management API - Lesson 7
-- Database schema: users, categories, tasks

-- 1. Drop old tables to rebuild the schema from scratch.
DROP TABLE IF EXISTS tasks CASCADE;
DROP TABLE IF EXISTS task CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- 2. Users table.
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       email VARCHAR(150) NOT NULL UNIQUE
);

-- 3. Categories table.
CREATE TABLE categories (
                            id BIGSERIAL PRIMARY KEY,
                            name VARCHAR(100) NOT NULL UNIQUE
);

-- 4. Tasks table.
CREATE TABLE tasks (
                       id BIGSERIAL PRIMARY KEY,
                       title VARCHAR(200) NOT NULL,
                       description VARCHAR(1000),
                       status VARCHAR(20) NOT NULL,
                       priority VARCHAR(20) NOT NULL,
                       user_id BIGINT NOT NULL REFERENCES users(id),
                       category_id BIGINT REFERENCES categories(id),
                       created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                       updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- 5. Indexes for frequently used searches.
CREATE INDEX idx_tasks_user_id ON tasks(user_id);
CREATE INDEX idx_tasks_category_id ON tasks(category_id);
CREATE INDEX idx_tasks_status ON tasks(status);
