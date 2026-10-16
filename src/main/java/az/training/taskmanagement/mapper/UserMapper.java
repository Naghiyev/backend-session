package az.training.taskmanagement.mapper;

import az.training.taskmanagement.dto.CreateUserRequest;
import az.training.taskmanagement.dto.UserResponse;
import az.training.taskmanagement.model.User;

public final class UserMapper {

    private UserMapper() {
    }

    public static User toEntity(CreateUserRequest request) {
        return new User(request.name(), request.email());
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }
}
