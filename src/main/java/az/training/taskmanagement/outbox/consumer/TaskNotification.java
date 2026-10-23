package az.training.taskmanagement.outbox.consumer;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

/**
 * Consumer-in "biznes nəticəsi": istifadəçiyə göstəriləcək bildiriş.
 * Real layihədə bu ayrı notification-service olardı (email/SMS/push).
 */
@Entity
@Table(name = "task_notification")
public class TaskNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false)
    private UUID eventId;

    @Column(name = "task_id", nullable = false)
    private Long taskId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected TaskNotification() {
    }

    public TaskNotification(UUID eventId, Long taskId, Long userId, String message) {
        this.eventId = eventId;
        this.taskId = taskId;
        this.userId = userId;
        this.message = message;
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public UUID getEventId() { return eventId; }
    public Long getTaskId() { return taskId; }
    public Long getUserId() { return userId; }
    public String getMessage() { return message; }
    public Instant getCreatedAt() { return createdAt; }
}
