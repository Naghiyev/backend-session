package az.training.taskmanagement.dto;

public record AuthResponse(String token, String type, String role) {
    public static AuthResponse bearer(String token, String role) {
        return new AuthResponse(token, "Bearer", role);
    }
}
