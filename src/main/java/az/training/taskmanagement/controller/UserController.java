package az.training.taskmanagement.controller;

import az.training.taskmanagement.dto.CreateUserRequest;
import az.training.taskmanagement.dto.TaskResponse;
import az.training.taskmanagement.dto.UserResponse;
import az.training.taskmanagement.service.TaskService;
import az.training.taskmanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * User REST controller.
 *
 * Lesson 2-dəki sadə class-a HTTP annotasiyaları əlavə edildi:
 *  - @RestController: hər metodun qaytardığı obyekt JSON-a çevrilir.
 *  - @RequestMapping("/users"): bu controller-in baza yolu.
 *  - Dependency-lər constructor ilə Spring tərəfindən inject olunur.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final TaskService taskService;

    public UserController(UserService userService, TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request) {
        UserResponse created = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public List<UserResponse> getAll() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}/tasks")
    public List<TaskResponse> getUserTasks(@PathVariable Long id) {
        return taskService.getTasksByUser(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
