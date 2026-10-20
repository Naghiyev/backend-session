package az.training.taskmanagement.exceptions;

/**
 * Axtarılan resurs (User, Task, Category) tapılmadıqda atılır.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " tapılmadı: id=" + id);
    }
}