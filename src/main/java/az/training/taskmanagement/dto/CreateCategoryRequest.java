package az.training.taskmanagement.dto;

/**
 * User yaratmaq üçün gələn məlumat (request DTO).
 * DTO domain model-dən ayrıdır: xarici dünya ilə model arasında sərhəd.
 */
public record CreateCategoryRequest(String name, String email) {
}
