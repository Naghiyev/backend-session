package az.training.taskmanagement.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Long id) {
        super("Category tapilmadi: id=" + id);
    }
}
