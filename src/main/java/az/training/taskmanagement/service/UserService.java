package az.training.taskmanagement.service;

import az.training.taskmanagement.dto.CreateUserRequest;
import az.training.taskmanagement.dto.PageResponse;
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

    List<UserResponse> getUsersByName(String name);

    PageResponse<UserResponse> getUsersByNamePaged(String name, int page, int size);

    void deleteUser(Long id);
}
