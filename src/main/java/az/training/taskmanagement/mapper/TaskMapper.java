package az.training.taskmanagement.mapper;

import az.training.taskmanagement.dto.CreateTaskRequest;
import az.training.taskmanagement.dto.TaskResponse;
import az.training.taskmanagement.model.Priority;
import az.training.taskmanagement.model.Task;
import az.training.taskmanagement.model.TaskStatus;
import az.training.taskmanagement.model.User;

public final class TaskMapper {

    private TaskMapper() {
    }

    public static Task toEntity(CreateTaskRequest request, User user) {
        Priority priority = request.priority() == null ? Priority.MEDIUM : request.priority();
        return new Task(request.title(), request.description(), TaskStatus.TODO, priority, user);
    }

    public static TaskResponse toResponse(Task task) {
        Long userId = task.getUser() != null ? task.getUser().getId() : null;
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                userId,
                task.getCreatedAt(),
                task.getUpdatedAt());
    }
}
