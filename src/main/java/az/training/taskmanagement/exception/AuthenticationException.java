package az.training.taskmanagement.exception;

/** Yanlış login məlumatları -> HTTP 401 (GlobalExceptionHandler-də). */
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }
}
