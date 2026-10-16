package az.training.taskmanagement.exception;

/** Business validation səhvi. HTTP 400 → GlobalExceptionHandler. */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
