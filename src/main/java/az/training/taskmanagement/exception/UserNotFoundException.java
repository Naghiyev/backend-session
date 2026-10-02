package az.training.taskmanagement.exception;

public class UserNotFoundException extends ResourceNotFoundException {

    public UserNotFoundException(Long id) {
        super("User", id);
    }
}
