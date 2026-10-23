package az.training.taskmanagement.outbox;

import az.training.taskmanagement.outbox.consumer.TaskNotification;
import az.training.taskmanagement.outbox.consumer.TaskNotificationRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Dərs/demo üçün: outbox cədvəlinin və consumer nəticələrinin vəziyyətinə baxmaq.
 *
 *  GET /outbox/events               son 50 event
 *  GET /outbox/events?status=PENDING
 *  GET /outbox/stats                status üzrə say
 *  GET /outbox/notifications        consumer-in yaratdığı bildirişlər
 */
@RestController
@RequestMapping("/outbox")
public class OutboxController {

    private final OutboxEventRepository outboxRepository;
    private final TaskNotificationRepository notificationRepository;

    public OutboxController(OutboxEventRepository outboxRepository,
                            TaskNotificationRepository notificationRepository) {
        this.outboxRepository = outboxRepository;
        this.notificationRepository = notificationRepository;
    }

    @GetMapping("/events")
    public List<OutboxEvent> events(@RequestParam(required = false) OutboxStatus status) {
        return status == null
                ? outboxRepository.findTop50ByOrderByCreatedAtDesc()
                : outboxRepository.findTop50ByStatusOrderByCreatedAtDesc(status);
    }

    @GetMapping("/stats")
    public Map<OutboxStatus, Long> stats() {
        return Map.of(
                OutboxStatus.PENDING, outboxRepository.countByStatus(OutboxStatus.PENDING),
                OutboxStatus.SENT, outboxRepository.countByStatus(OutboxStatus.SENT),
                OutboxStatus.FAILED, outboxRepository.countByStatus(OutboxStatus.FAILED));
    }

    @GetMapping("/notifications")
    public List<TaskNotification> notifications() {
        return notificationRepository.findTop50ByOrderByCreatedAtDesc();
    }
}
