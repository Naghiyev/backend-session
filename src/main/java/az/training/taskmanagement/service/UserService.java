package az.training.taskmanagement.service;

import az.training.taskmanagement.dto.CreateUserRequest;
import az.training.taskmanagement.dto.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * User business logic kontraktı.
 * Service DTO qəbul edir və DTO qaytarır - domain model xaricə "sızmır".
 */
public interface UserService {

    UserResponse createUser(CreateUserRequest request);

    UserResponse getUserById(Long id);

    List<UserResponse> getAllUsers();

    void deleteUser(Long id);

    Page<UserResponse> getUsersByName(
            String name, Pageable pageable);
}
