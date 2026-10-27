package az.training.taskmanagement.exception;

public class TaskLimitExceededException extends RuntimeException {

    public TaskLimitExceededException(Long userId, int limit) {
        super("User-in aktiv task limiti aşılıb (maksimum " + limit + "): userId=" + userId);
    }
}