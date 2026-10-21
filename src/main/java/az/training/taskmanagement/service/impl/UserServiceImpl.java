package az.training.taskmanagement.service.impl;

import az.training.taskmanagement.dto.CreateUserRequest;
import az.training.taskmanagement.dto.UserResponse;
import az.training.taskmanagement.exception.DuplicateResourceException;
import az.training.taskmanagement.exception.ResourceNotFoundException;
import az.training.taskmanagement.exception.ValidationException;
import az.training.taskmanagement.mapper.UserMapper;
import az.training.taskmanagement.model.User;
import az.training.taskmanagement.repository.UserRepository;
import az.training.taskmanagement.service.UserService;

import java.util.List;
import java.util.Locale;

@org.springframework.stereotype.Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    // Constructor injection - dependency yalnız interface-dir.
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
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

    @Override
    public UserResponse getUserById(Long id) {
        return UserMapper.toResponse(findUserOrThrow(id));
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    @Override
    public List<UserResponse> searchUsers(String name) {
        if (name == null || name.isBlank()) {
            return getAllUsers();
        }
        String query = name.strip().toLowerCase();
        return userRepository.findAll().stream()
                .filter(user -> user.getName().toLowerCase().contains(query))
                .map(UserMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteUser(Long id) {
        findUserOrThrow(id);
        userRepository.deleteById(id);
    }

    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("User", id));
    }
}
