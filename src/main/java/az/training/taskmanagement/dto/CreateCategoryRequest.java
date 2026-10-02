package az.training.taskmanagement.dto;

/**
 * Category yaratmaq üçün gələn məlumat (request DTO).
 */
public record CreateCategoryRequest(String name, String description) {
}
