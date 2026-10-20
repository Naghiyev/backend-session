package az.training.taskmanagement.mapper;

import az.training.taskmanagement.dto.CreateTaskRequest;
import az.training.taskmanagement.dto.TaskResponse;
import az.training.taskmanagement.model.*;

public final class TaskMapper {

    private TaskMapper() {
    }

    public static Task toEntity(CreateTaskRequest request, User user, Category category) {
        Priority priority = request.priority() == null ? Priority.MEDIUM : request.priority();
        Task task = new Task(request.title(), request.description(), TaskStatus.TODO, priority, user);
        task.setCategory(category);
        return task;
    }

    public static TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getUser() != null ? task.getUser().getId() : null,
                task.getCategory() != null ? task.getCategory().getId() : null,
                task.getCreatedAt(),
                task.getUpdatedAt());
    }
}
