package az.training.taskmanagement.service;

import az.training.taskmanagement.dto.CreateUserRequest;
import az.training.taskmanagement.dto.PageResponse;
import az.training.taskmanagement.dto.UserResponse;

import java.util.List;


public interface UserService {

    UserResponse createUser(CreateUserRequest request);

    UserResponse getUserById(Long id);

    List<UserResponse> getAllUsers();

    void deleteUser(Long id);

    List<UserResponse> getUserByName(String name);

    PageResponse<UserResponse> getUsersByNamePaged(String name, int page, int size);
}
