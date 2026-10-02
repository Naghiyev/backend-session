package az.training.taskmanagement.exception;

public class ValidationException extends TaskManagementException {

    private final String field;

    public ValidationException(String field, String message) {
        super(field + ": " + message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
