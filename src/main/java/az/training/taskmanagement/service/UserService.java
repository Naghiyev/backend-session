package az.training.taskmanagement.service;

import az.training.taskmanagement.exception.DuplicateEmailException;
import az.training.taskmanagement.exception.ResourceInUseException;
import az.training.taskmanagement.exception.UserNotFoundException;
import az.training.taskmanagement.model.User;
import az.training.taskmanagement.repository.TaskRepository;
import az.training.taskmanagement.repository.UserRepository;

import java.util.List;

import static az.training.taskmanagement.util.ValidationUtils.*;
/**
 * User üçün business logic.
 *
 * Lesson 1: service repository-ni istifadə edir və sadə qaydaları (validation)
 * tətbiq edir. Hələ framework yoxdur - dependency əl ilə constructor-a verilir.
 */
public class UserService {

    private static final int NAME_MAX_LENGTH = 100;

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public UserService(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    public User createUser(String name, String email) {
        String validName = requireNonBlank(name, "name", NAME_MAX_LENGTH);
        String validEmail = requireValidEmail(email);
        if (userRepository.existsByEmail(validEmail)) {
            throw new DuplicateEmailException(validEmail);
        }
        User user = User.builder()
                .name(validName)
                .email(validEmail)
                .build();
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        requireNonNull(id, "id");
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        getUserById(id); // mövcudluğu yoxla
        if (taskRepository.existsByUserId(id)) {
            throw new ResourceInUseException(
                    "User silinə bilməz, ona aid task-lar var: id=" + id);
        }
        userRepository.deleteById(id);
    }
}
