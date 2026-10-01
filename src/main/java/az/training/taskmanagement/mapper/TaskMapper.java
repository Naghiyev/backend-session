package az.training.taskmanagement.mapper;

import az.training.taskmanagement.dto.CreateTaskRequest;
import az.training.taskmanagement.dto.TaskResponse;
import az.training.taskmanagement.model.Priority;
import az.training.taskmanagement.model.Task;
import az.training.taskmanagement.model.TaskStatus;

public final class TaskMapper {

    private TaskMapper() {
    }

    public static Task toEntity(CreateTaskRequest request) {
        Priority priority = request.priority() == null ? Priority.MEDIUM : request.priority();
        return new Task.Builder()
                .title(request.title())
                .description(request.description())
                .status(TaskStatus.TODO)
                .priority(priority)
                .userId(request.userId())
                .build();
    }

    public static TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getUserId(),
                task.getCreatedAt(),
                task.getUpdatedAt());
    }
}
