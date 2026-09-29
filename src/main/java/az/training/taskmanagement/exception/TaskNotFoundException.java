package az.training.taskmanagement.exception;

public class TaskNotFoundException extends NotFoundException {

    public TaskNotFoundException(Long id) {
        super("Task tapılmadı: id=" + id);
    }
}