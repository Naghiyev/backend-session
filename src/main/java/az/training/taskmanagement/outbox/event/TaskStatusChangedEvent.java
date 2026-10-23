package az.training.taskmanagement.outbox.event;

import az.training.taskmanagement.model.Task;
import az.training.taskmanagement.model.TaskStatus;

import java.time.Instant;

/** "Task-ın statusu dəyişdi" faktı (məs. TODO → IN_PROGRESS). */
public record TaskStatusChangedEvent(
        int schemaVersion,
        Long taskId,
        Long userId,
        TaskStatus oldStatus,
        TaskStatus newStatus,
        Instant occurredAt) {

    public static final String TYPE = "TaskStatusChanged";

    public static TaskStatusChangedEvent of(Task task, TaskStatus oldStatus) {
        return new TaskStatusChangedEvent(1, task.getId(), task.getUser().getId(),
                oldStatus, task.getStatus(), Instant.now());
    }
}
