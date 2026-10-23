package az.training.taskmanagement.outbox;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Polling publisher: outbox cədvəlindən PENDING eventləri götürüb Kafka-ya göndərir.
 *
 * Bir dövr = bir DB tranzaksiyası:
 *   1) SKIP LOCKED ilə partiyanı kilidlə
 *   2) hər eventi Kafka-ya göndər və broker təsdiqini (ack) gözlə
 *   3) SENT və ya retry məlumatını yaz
 *   4) commit → kilidlər buraxılır
 *
 * Zəmanət: at-least-once. Göndərib SENT yazmadan app ölərsə, event yenidən göndəriləcək.
 * Ona görə consumer idempotent olmalıdır (bax: consumer/TaskEventsListener).
 */
@Component
public class OutboxRelay {

    private static final Logger log = LoggerFactory.getLogger(OutboxRelay.class);

    private final OutboxEventRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final TransactionTemplate transactionTemplate;
    private final OutboxProperties props;

    public OutboxRelay(OutboxEventRepository repository,
                       KafkaTemplate<String, String> kafkaTemplate,
                       TransactionTemplate transactionTemplate,
                       OutboxProperties props) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
        this.transactionTemplate = transactionTemplate;
        this.props = props;
    }

    @Scheduled(fixedDelayString = "${outbox.poll-interval:1000}")
    public void publishPending() {
        Integer sent;
        do {
            sent = transactionTemplate.execute(status -> publishBatch());
        } while (sent != null && sent == props.batchSize()); // partiya tam doludursa, gözləmədən növbətini götür
    }

    private int publishBatch() {
        List<OutboxEvent> batch = repository.lockNextBatch(props.batchSize());
        if (batch.isEmpty()) {
            return 0;
        }

        int sent = 0;
        for (OutboxEvent event : batch) {
            try {
                kafkaTemplate.send(toRecord(event))
                        .get(props.sendTimeout().toMillis(), TimeUnit.MILLISECONDS);
                event.markSent();
                sent++;
                log.debug("Outbox event göndərildi: {}", event);
            } catch (Exception ex) {
                if (ex instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
                Throwable cause = (ex instanceof ExecutionException && ex.getCause() != null) ? ex.getCause() : ex;
                event.markFailedAttempt(cause.toString(), props.maxAttempts());
                if (event.getStatus() == OutboxStatus.FAILED) {
                    log.error("Outbox event FAILED oldu, manual baxış lazımdır: {} - {}", event, cause.toString());
                } else {
                    log.warn("Outbox event göndərilmədi (cəhd {}), növbəti cəhd {}: {}",
                            event.getAttempts(), event.getNextAttemptAt(), cause.toString());
                }
                // Partiyanın qalanını göndərmirik:
                //  - Kafka dayanıbsa, hər event üçün timeout gözləyib tranzaksiyanı uzatmağın mənası yoxdur;
                //  - sonrakı eventlər (məs. eyni task-ın TaskStatusChanged-i) bundan qabağa keçməsin.
                // Qalan sətirlər PENDING qalır və növbəti dövrdə yenidən götürülür.
                break;
            }
        }
        if (sent > 0) {
            log.info("Outbox relay: {} event Kafka-ya göndərildi", sent);
        }
        return sent;
    }

    private ProducerRecord<String, String> toRecord(OutboxEvent e) {
        String topic = e.getAggregateType() + ".events";   // "task" → "task.events"
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, e.getAggregateId(), e.getPayload());
        record.headers()
                .add("eventId", e.getId().toString().getBytes(StandardCharsets.UTF_8))
                .add("eventType", e.getEventType().getBytes(StandardCharsets.UTF_8));
        return record;
    }
}
