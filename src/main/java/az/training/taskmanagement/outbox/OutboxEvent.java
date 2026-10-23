package az.training.taskmanagement.outbox;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

/**
 * Outbox cədvəlinin bir sətri = Kafka-ya göndəriləcək bir event.
 *
 * Əsas qayda: bu sətir biznes dəyişikliyi (məs. Task INSERT) ilə EYNİ tranzaksiyada yazılır.
 * Ya ikisi də commit olunur, ya heç biri. Kafka-ya göndərmə isə sonra, ayrıca prosesdə (OutboxRelay) olur.
 */
@Entity
@Table(name = "outbox_event",
        indexes = @Index(name = "ix_outbox_status_next_attempt", columnList = "status, next_attempt_at"))
public class OutboxEvent {

    /** eventId: consumer bu ID ilə dublikatları ayırır. */
    @Id
    private UUID id;

    /** Məs. "task" → topic adı "task.events" olur. */
    @Column(name = "aggregate_type", nullable = false, length = 64)
    private String aggregateType;

    /** Məs. taskId. Kafka key kimi istifadə olunur → eyni task-ın eventləri eyni partition-a düşür (sıra qorunur). */
    @Column(name = "aggregate_id", nullable = false, length = 64)
    private String aggregateId;

    /** Məs. "TaskCreated", "TaskStatusChanged". */
    @Column(name = "event_type", nullable = false, length = 64)
    private String eventType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String payload;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private OutboxStatus status;

    @Column(nullable = false)
    private int attempts;

    @Column(name = "next_attempt_at", nullable = false)
    private Instant nextAttemptAt;

    @Column(name = "last_error", length = 1000)
    private String lastError;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "published_at")
    private Instant publishedAt;

    protected OutboxEvent() {
        // JPA üçün
    }

    public static OutboxEvent pending(String aggregateType, String aggregateId, String eventType, String payload) {
        OutboxEvent e = new OutboxEvent();
        e.id = UUID.randomUUID();
        e.aggregateType = aggregateType;
        e.aggregateId = aggregateId;
        e.eventType = eventType;
        e.payload = payload;
        e.status = OutboxStatus.PENDING;
        e.attempts = 0;
        e.createdAt = Instant.now();
        e.nextAttemptAt = e.createdAt;
        return e;
    }

    /** Kafka göndərməni təsdiqlədi. */
    public void markSent() {
        this.status = OutboxStatus.SENT;
        this.publishedAt = Instant.now();
        this.lastError = null;
    }

    /** Göndərmə alınmadı: exponential backoff ilə növbəti cəhd və ya FAILED. */
    public void markFailedAttempt(String error, int maxAttempts) {
        this.attempts++;
        this.lastError = error == null ? null : error.substring(0, Math.min(error.length(), 1000));
        if (attempts >= maxAttempts) {
            this.status = OutboxStatus.FAILED;
        } else {
            long backoffSeconds = Math.min(300, (long) Math.pow(2, attempts)); // 2, 4, 8 ... 300 san
            this.nextAttemptAt = Instant.now().plusSeconds(backoffSeconds);
        }
    }

    public UUID getId() { return id; }
    public String getAggregateType() { return aggregateType; }
    public String getAggregateId() { return aggregateId; }
    public String getEventType() { return eventType; }
    public String getPayload() { return payload; }
    public OutboxStatus getStatus() { return status; }
    public int getAttempts() { return attempts; }
    public Instant getNextAttemptAt() { return nextAttemptAt; }
    public String getLastError() { return lastError; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getPublishedAt() { return publishedAt; }

    @Override
    public String toString() {
        return "OutboxEvent{id=" + id + ", type=" + eventType + ", aggregateId=" + aggregateId
                + ", status=" + status + ", attempts=" + attempts + "}";
    }
}
