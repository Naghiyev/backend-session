package az.training.taskmanagement.exceptions;

/**
 * Unikal olmalı dəyər (məs. email, category adı) artıq mövcud olduqda atılır.
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}