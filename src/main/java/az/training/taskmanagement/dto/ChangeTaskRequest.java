package az.training.taskmanagement.dto;

import az.training.taskmanagement.model.Priority;
import az.training.taskmanagement.model.TaskStatus;

public record ChangeTaskRequest(String title, String description, TaskStatus status, Priority priority, Long userId, Long categoryId) {
}
