package az.training.taskmanagement.exception;

public class DuplicateCategoryException extends DuplicateResourceException {

    public DuplicateCategoryException(String name) {
        super("Bu adda category artıq mövcuddur: " + name);
    }
}