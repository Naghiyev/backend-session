package az.training.taskmanagement.dto;

import az.training.taskmanagement.model.Priority;
import az.training.taskmanagement.model.TaskStatus;

import java.time.LocalDateTime;

/**
 * Xaricə qaytarılan Task məlumatı.
 */
public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskStatus status,
        Priority priority,
        Long userId,
        Long categoryId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
