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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

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
        log.info("User yaradıldı: id={}, email={}", saved.getId(), saved.getEmail());
        return UserMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        return UserMapper.toResponse(findUserOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteUser(Long id) {
        findUserOrThrow(id);
        userRepository.deleteById(id);
        log.info("User silindi: id={}", id);
    }

    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("User", id));
    }
}
