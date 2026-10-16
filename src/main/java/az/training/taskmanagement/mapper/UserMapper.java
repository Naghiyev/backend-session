package az.training.taskmanagement.mapper;

import az.training.taskmanagement.dto.CreateUserRequest;
import az.training.taskmanagement.dto.UserResponse;
import az.training.taskmanagement.model.User;

/**
 * User <-> DTO çevrilməsi. Mapping-i bir yerdə saxlamaq üçün.
 */
public final class UserMapper {

    private UserMapper() {
    }

    public static User toEntity(CreateUserRequest request) {
        User user = new User();
        user.setName(request.name().trim());
        user.setEmail(request.email().trim().toLowerCase());
        return user;
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }
}
