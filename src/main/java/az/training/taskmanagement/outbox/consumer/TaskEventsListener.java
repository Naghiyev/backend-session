package az.training.taskmanagement.outbox.consumer;

import az.training.taskmanagement.outbox.KafkaConfig;
import az.training.taskmanagement.outbox.event.TaskCreatedEvent;
import az.training.taskmanagement.outbox.event.TaskStatusChangedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * İdempotent consumer: "task.events" topic-ini oxuyur və bildiriş yaradır.
 *
 * Outbox at-least-once çatdırır, yəni eyni event 2 dəfə gələ bilər.
 * Ona görə hər eventin eventId-si processed_message cədvəlinə yazılır:
 *   - INSERT uğurlu oldu (1 sətir)  → ilk dəfədir, emal edirik
 *   - INSERT heç nə yazmadı (0)      → dublikatdır, ötürürük
 * Dedup INSERT-i və bildiriş INSERT-i EYNİ tranzaksiyadadır: ya ikisi də yazılır, ya heç biri.
 */
@Component
public class TaskEventsListener {

    private static final Logger log = LoggerFactory.getLogger(TaskEventsListener.class);
    private static final String CONSUMER_NAME = "task-notification";

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;
    private final TaskNotificationRepository notificationRepository;

    public TaskEventsListener(JdbcTemplate jdbcTemplate,
                              ObjectMapper objectMapper,
                              TaskNotificationRepository notificationRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
        this.notificationRepository = notificationRepository;
    }

    @KafkaListener(topics = KafkaConfig.TASK_EVENTS_TOPIC, groupId = "task-notification")
    @Transactional
    public void onEvent(ConsumerRecord<String, String> record) throws JsonProcessingException {
        UUID eventId = UUID.fromString(header(record, "eventId"));
        String eventType = header(record, "eventType");

        int inserted = jdbcTemplate.update("""
                INSERT INTO processed_message (event_id, consumer, processed_at)
                VALUES (?, ?, now())
                ON CONFLICT (event_id) DO NOTHING
                """, eventId, CONSUMER_NAME);

        if (inserted == 0) {
            log.info("Dublikat event ötürüldü: eventId={}, type={}", eventId, eventType);
            return;
        }

        switch (eventType) {
            case TaskCreatedEvent.TYPE -> {
                TaskCreatedEvent e = objectMapper.readValue(record.value(), TaskCreatedEvent.class);
                save(eventId, e.taskId(), e.userId(),
                        "Sizə yeni task təyin olundu: \"" + e.title() + "\" (prioritet: " + e.priority() + ")");
            }
            case TaskStatusChangedEvent.TYPE -> {
                TaskStatusChangedEvent e = objectMapper.readValue(record.value(), TaskStatusChangedEvent.class);
                save(eventId, e.taskId(), e.userId(),
                        "Task #" + e.taskId() + " statusu dəyişdi: " + e.oldStatus() + " → " + e.newStatus());
            }
            default -> log.warn("Naməlum event tipi ötürüldü: {}", eventType);
        }
    }

    private void save(UUID eventId, Long taskId, Long userId, String message) {
        notificationRepository.save(new TaskNotification(eventId, taskId, userId, message));
        log.info("Bildiriş yaradıldı: userId={}, {}", userId, message);
    }

    private static String header(ConsumerRecord<?, ?> record, String name) {
        Header h = record.headers().lastHeader(name);
        if (h == null) {
            throw new IllegalArgumentException("Kafka header yoxdur: " + name);
        }
        return new String(h.value(), StandardCharsets.UTF_8);
    }
}
