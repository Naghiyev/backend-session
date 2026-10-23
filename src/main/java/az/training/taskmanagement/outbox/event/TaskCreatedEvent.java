package az.training.taskmanagement.outbox.event;

import az.training.taskmanagement.model.Priority;
import az.training.taskmanagement.model.Task;
import az.training.taskmanagement.model.TaskStatus;

import java.time.Instant;

/**
 * "Task yaradıldı" faktı. Event adı keçmiş zamanda olur, çünki artıq baş vermiş hadisəni bildirir.
 * schemaVersion: format sonradan dəyişsə, consumer köhnə və yeni versiyanı ayıra bilsin.
 */
public record TaskCreatedEvent(
        int schemaVersion,
        Long taskId,
        Long userId,
        String title,
        TaskStatus status,
        Priority priority,
        Instant occurredAt) {

    public static final String TYPE = "TaskCreated";

    public static TaskCreatedEvent from(Task task) {
        return new TaskCreatedEvent(1, task.getId(), task.getUser().getId(), task.getTitle(),
                task.getStatus(), task.getPriority(), Instant.now());
    }
}
