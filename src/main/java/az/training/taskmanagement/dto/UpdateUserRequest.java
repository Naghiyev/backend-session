package az.training.taskmanagement.dto;

/**
 * User yeniləmək üçün gələn məlumat (request DTO).
 */
public record UpdateUserRequest(String name, String email) {
}
