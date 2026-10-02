package az.training.taskmanagement.exception;

public class CategoryNotFoundException extends ResourceNotFoundException {

    public CategoryNotFoundException(Long id) {
        super("Category", id);
    }
}
