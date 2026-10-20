package az.training.taskmanagement.dto;

import az.training.taskmanagement.model.Priority;
import az.training.taskmanagement.model.TaskStatus;

public record FullUpdateTaskRequest(String title, String description, TaskStatus status, Priority priority) {
}