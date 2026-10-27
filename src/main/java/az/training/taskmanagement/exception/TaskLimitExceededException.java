package az.training.taskmanagement.exception;

public class TaskLimitExceededException extends RuntimeException {
    public TaskLimitExceededException(String message) {
        super(message);
    }
    public static TaskLimitExceededException of(String entity, Object id) {
        return new TaskLimitExceededException(entity + " task limit exceeded, conflict" + id);
    }
}
