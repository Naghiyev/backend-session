package az.training.taskmanagement.controller;

import az.training.taskmanagement.dto.CreateUserRequest;
import az.training.taskmanagement.dto.TaskResponse;
import az.training.taskmanagement.dto.UserResponse;
import az.training.taskmanagement.service.TaskService;
import az.training.taskmanagement.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Lesson 5 — fundamental endpoints only.
 * Qalan endpoint-lər (getAll, delete, user-in taskları) Lesson 6-da əlavə olunur.
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

    @GetMapping
    public List<UserResponse> getAll(@RequestParam(required = false) String name,
                                     @RequestParam(required = false) String email) {
        return userService.searchUsers(name, email);
    }

    @GetMapping("/{id}/tasks")
    public List<TaskResponse> getUserTasks(@PathVariable Long id) {
        return taskService.getTasksByUser(id);
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}
