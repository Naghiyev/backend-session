package az.training.taskmanagement.dto;

/**
 * Category yeniləmək üçün gələn məlumat (request DTO).
 */
public record UpdateCategoryRequest(String name, String description) {
}
