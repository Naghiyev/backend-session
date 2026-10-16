package az.training.taskmanagement.dto;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Bütün xətalar üçün standart cavab modeli.
 * fieldErrors yalnız validation səhvlərində dolur.
 */
public record ApiError(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> fieldErrors
) {
    public static ApiError of(int status, String error, String message, String path) {
        return new ApiError(LocalDateTime.now(), status, error, message, path, null);
    }
}
