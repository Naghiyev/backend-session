package az.training.taskmanagement.outbox;

/**
 * Outbox sətrinin həyat dövrü:
 *  PENDING → (relay Kafka-ya göndərdi) → SENT
 *  PENDING → (max cəhd sayı keçildi)    → FAILED  (manual baxış lazımdır)
 */
public enum OutboxStatus {
    PENDING,
    SENT,
    FAILED
}
