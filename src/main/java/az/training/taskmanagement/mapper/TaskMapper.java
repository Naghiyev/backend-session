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
        return new Task(null, request.title(), request.description(),
                TaskStatus.TODO, priority, request.userId(),request.categoryId());
    }

    public static TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getUserId(),
                task.getCategoryId(),
                task.getCreatedAt(),
                task.getUpdatedAt());
    }
}
