package az.training.taskmanagement.exception;

public class TaskLimitExceededException extends RuntimeException {
    public TaskLimitExceededException(String message) {
        super(message);
    }
}
