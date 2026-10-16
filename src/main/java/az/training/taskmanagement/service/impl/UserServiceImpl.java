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
import az.training.taskmanagement.dto.PaginationResponse;
import java.util.Comparator;

import java.util.List;

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
    public PaginationResponse<UserResponse> searchUsers(String name, int page, int size) {
        if (page < 0) {
            throw new ValidationException("page 0-dan kiçik ola bilməz");
        }
        if (size < 1 || size > 100) {
            throw new ValidationException("size 1 ilə 100 arasında olmalıdır");
        }

        List<User> users = (name == null || name.isBlank())
                ? userRepository.findAll()
                : userRepository.findUsersEqualsGivenName(name);

        List<UserResponse> content = users.stream()
                .sorted(Comparator.comparing(User::getId))
                .skip((long) page * size)
                .limit(size)
                .map(UserMapper::toResponse)
                .toList();

        long totalElements = users.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        return new PaginationResponse<>(content, page, size, totalElements, totalPages);
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
