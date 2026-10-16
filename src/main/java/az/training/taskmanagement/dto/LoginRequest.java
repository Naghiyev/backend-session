package az.training.taskmanagement.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "username tələb olunur") String username,
        @NotBlank(message = "password tələb olunur") String password
) {
}
