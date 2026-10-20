package az.training.taskmanagement.exceptions;

/**
 * Daxil edilən məlumat yanlış və ya boş olduqda atılır.
 */
public class InvalidInputException extends RuntimeException {

    public InvalidInputException(String message) {
        super(message);
    }
}