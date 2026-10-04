package az.training.taskmanagement.mapper;

import az.training.taskmanagement.dto.user.request.CreateUserRequest;
import az.training.taskmanagement.dto.user.response.UserResponse;
import az.training.taskmanagement.model.User;

/**
 * User <-> DTO çevrilməsi. Mapping-i bir yerdə saxlamaq üçün.
 */
public final class UserMapper {

    private UserMapper() {
    }

    public static User toEntity(CreateUserRequest request) {
        return new User(null, request.name(), request.email());
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }
}
