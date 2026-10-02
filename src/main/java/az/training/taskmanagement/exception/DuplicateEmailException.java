package az.training.taskmanagement.exception;

public class DuplicateEmailException extends DuplicateResourceException {

    public DuplicateEmailException(String email) {
        super("Bu email artıq mövcuddur: " + email);
    }
}
