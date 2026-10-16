package az.training.taskmanagement.exception;

/**
 * Resurs tapılmadıqda. HTTP status kodu artıq GlobalExceptionHandler-də
 * mərkəzləşdirilib (Lesson 6) - burada @ResponseStatus lazım deyil.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException of(String entity, Object id) {
        return new ResourceNotFoundException(entity + " tapılmadı: id=" + id);
    }
}
