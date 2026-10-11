package az.training.taskmanagement.dto;

import az.training.taskmanagement.model.Priority;

/**
 * Task yaratmaq üçün gələn məlumat.
 */
public record CreateTaskRequest(String title, String description, Priority priority, Long userId,Long categoryId) {
}
