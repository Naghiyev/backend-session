package az.training.taskmanagement.exception;

/** Unikal dəyər təkrarı. HTTP 409 → GlobalExceptionHandler. */
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
