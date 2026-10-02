package az.training.taskmanagement.exception;

public class TaskNotFoundException extends ResourceNotFoundException {

    public TaskNotFoundException(Long id) {
        super("Task", id);
    }
}
