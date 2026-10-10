package az.training.taskmanagement.service;

import az.training.taskmanagement.dto.CreateUserRequest;
import az.training.taskmanagement.dto.UpdateUserRequest;
import az.training.taskmanagement.dto.UserResponse;
import az.training.taskmanagement.exception.DuplicateResourceException;
import az.training.taskmanagement.exception.ResourceNotFoundException;
import az.training.taskmanagement.exception.ValidationException;
import az.training.taskmanagement.mapper.UserMapper;
import az.training.taskmanagement.model.User;
import az.training.taskmanagement.repository.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserService {

    private final UserRepository userRepository;

    // Constructor injection - dependency yalnız interface-dir.
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse createUser(CreateUserRequest request) {
        if (request.name() == null || request.name().isBlank()) {
            throw new ValidationException("name boş ola bilməz");
        }
        if (request.email() == null || request.email().isBlank()) {
            throw new ValidationException("email boş ola bilməz");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Bu email artıq mövcuddur: " + request.email());
        }
        User saved = userRepository.save(UserMapper.toEntity(request));
        return UserMapper.toResponse(saved);
    }

    public UserResponse getUserById(Long id) {
        return UserMapper.toResponse(findUserOrThrow(id));
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    public void deleteUser(Long id) {
        findUserOrThrow(id);
        userRepository.deleteById(id);
    }

    public UserResponse updateUser(Long id, UpdateUserRequest request){
        User user = findUserOrThrow(id);
        if (request.name() == null || request.name().isBlank()) {
            throw new ValidationException("name boş ola bilməz");
        }
        if (request.email() == null || request.email().isBlank()) {
            throw new ValidationException("email boş ola bilməz");
        }
//        if (userRepository.existsByEmail(request.email())) {
//
//            throw new DuplicateResourceException("Bu email artıq mövcuddur: " + request.email());
//        }

        Optional<User> existingUser =
                userRepository.findByEmail(request.email());

        if (existingUser.isPresent()
                && !existingUser.get().getId().equals(id)) {

            throw new DuplicateResourceException("Bu email artıq mövcuddur: " + request.email());
        }

        user.setName(request.name());
        user.setEmail(request.email());
        User updatedUser = userRepository.save(user);
        return UserMapper.toResponse(updatedUser);
    }


    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("User", id));
    }
}
