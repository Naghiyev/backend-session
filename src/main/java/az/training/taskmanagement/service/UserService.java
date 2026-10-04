package az.training.taskmanagement.service;

import az.training.taskmanagement.exception.EmailAlreadyExistsException;
import az.training.taskmanagement.exception.InvalidUserException;
import az.training.taskmanagement.exception.UserNotFoundException;
import az.training.taskmanagement.model.User;
import az.training.taskmanagement.repository.UserRepository;

import java.util.List;

/**
 * User üçün business logic.
 * <p>
 * Lesson 1: service repository-ni istifadə edir və sadə qaydaları (validation)
 * tətbiq edir. Hələ framework yoxdur - dependency əl ilə constructor-a verilir.
 */
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String name, String email) {
        if (name == null || name.isBlank()) {
            throw new InvalidUserException("name boş ola bilməz");
        }
        if (email == null || email.isBlank()) {
            throw new InvalidUserException("email boş ola bilməz");
        }
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Bu email artıq mövcuddur: " + email);
        }
        User user = new User(null, name, email);
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User tapılmadı: id=" + id));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        getUserById(id); // mövcudluğu yoxla
        userRepository.deleteById(id);
    }
}
