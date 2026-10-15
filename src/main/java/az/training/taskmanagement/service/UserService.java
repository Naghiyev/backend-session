package az.training.taskmanagement.service;

import az.training.taskmanagement.dto.CreateUserRequest;
import az.training.taskmanagement.dto.UserResponse;

import java.util.List;

/**
 * User business logic kontraktı.
 * Service DTO qəbul edir və DTO qaytarır - domain model xaricə "sızmır".
 */
public interface UserService {

    UserResponse createUser(CreateUserRequest request);

    UserResponse getUserById(Long id);

    List<UserResponse> getAllUsers();

    List<UserResponse> searchUsers(String name);

    void deleteUser(Long id);
}
