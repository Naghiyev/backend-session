package az.training.taskmanagement.controller;

import az.training.taskmanagement.dto.AuthResponse;
import az.training.taskmanagement.dto.LoginRequest;
import az.training.taskmanagement.exception.AuthenticationException;
import az.training.taskmanagement.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication endpoint.
 *
 * Introductory səviyyə: demo istifadəçi məlumatları application.properties-dən gəlir.
 * Real layihədə istifadəçilər database-də saxlanır və parol hash-lənir (BCrypt).
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;
    private final String demoUsername;
    private final String demoPassword;
    private final String demoRole;

    public AuthController(
            JwtService jwtService,
            @Value("${app.security.demo-username}") String demoUsername,
            @Value("${app.security.demo-password}") String demoPassword,
            @Value("${app.security.demo-role}") String demoRole) {
        this.jwtService = jwtService;
        this.demoUsername = demoUsername;
        this.demoPassword = demoPassword;
        this.demoRole = demoRole;
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        if (!demoUsername.equals(request.username()) || !demoPassword.equals(request.password())) {
            throw new AuthenticationException("username və ya password yanlışdır");
        }
        String token = jwtService.generateToken(request.username(), demoRole);
        return AuthResponse.bearer(token, demoRole);
    }
}
