package az.training.taskmanagement.dto.user.request;

/**
 * User yaratmaq üçün gələn məlumat (request DTO).
 * DTO domain model-dən ayrıdır: xarici dünya ilə model arasında sərhəd.
 */
public record CreateUserRequest(String name, String email) {
}
