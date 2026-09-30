package az.training.taskmanagement.exception;

public class CategoryNotFoundException extends NotFoundException {

    public CategoryNotFoundException(Long id) {
        super("Category tapılmadı: id=" + id);
    }
}
