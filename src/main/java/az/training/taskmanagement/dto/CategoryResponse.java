package az.training.taskmanagement.dto;

import java.time.LocalDateTime;

public record CategoryResponse(Long id,
                               String name,
                               String description,
                               String color,
                               boolean active,
                               LocalDateTime createdAt) {
}