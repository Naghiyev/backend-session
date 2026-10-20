package az.training.taskmanagement.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Task domain model.
 *
 * Task bir User-ə aiddir (userId vasitəsilə).
 * createdAt / updatedAt audit sahələridir.
 */
@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private LocalDateTime updatedAt;

    public Task(String title, String description, TaskStatus status, Priority priority, User user) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.user = user;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task task)) return false;
        return id != null && Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Task{id=" + id + ", title='" + title + "', status=" + status
                + ", priority=" + priority + "}";
    }
}
