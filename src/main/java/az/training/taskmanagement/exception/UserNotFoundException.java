package az.training.taskmanagement.exception;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(Long id) {
        super("User tapılmadı: id=" + id);
    }
}