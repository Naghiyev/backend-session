package az.training.taskmanagement.outbox.consumer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Inbox (dedup) cədvəli: consumer-in artıq emal etdiyi eventId-lər.
 * Primary key eventId olduğu üçün eyni event ikinci dəfə INSERT oluna bilmir.
 */
@Entity
@Table(name = "processed_message")
public class ProcessedMessage {

    @Id
    @Column(name = "event_id")
    private UUID eventId;

    @Column(nullable = false, length = 64)
    private String consumer;

    @Column(name = "processed_at", nullable = false)
    private Instant processedAt;

    protected ProcessedMessage() {
    }

    public UUID getEventId() { return eventId; }
    public String getConsumer() { return consumer; }
    public Instant getProcessedAt() { return processedAt; }
}
