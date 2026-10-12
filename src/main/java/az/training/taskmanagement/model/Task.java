package az.training.taskmanagement.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Task domain model.
 *
 * Task bir User-ə aiddir (userId vasitəsilə).
 * createdAt / updatedAt audit sahələridir.
 */
public class Task {

    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private Priority priority;
    private Long userId;
    private Long categoryId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Task() {
    }

    public Task(Long id, String title, String description,
                TaskStatus status, Priority priority, Long userId , Long categoryId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.userId = userId;
        this.categoryId = categoryId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task task)) return false;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Task{id=" + id + ", title='" + title + "', status=" + status
                + ", priority=" + priority + ", userId=" + userId + "}";
    }
}
