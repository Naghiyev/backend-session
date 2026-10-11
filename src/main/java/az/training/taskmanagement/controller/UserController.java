package az.training.taskmanagement.controller;

import az.training.taskmanagement.dto.CreateUserRequest;
import az.training.taskmanagement.dto.TaskResponse;
import az.training.taskmanagement.dto.UpdateUserRequest;
import az.training.taskmanagement.dto.UserResponse;
import az.training.taskmanagement.service.TaskService;
import az.training.taskmanagement.service.UserService;

import java.util.List;

/**
 * Controller = sistemin xarici sərhədi (boundary).
 *
 * Lesson 2-də bu sadə Java class-dır və HTTP haqqında heç nə bilmir -
 * yalnız request-i qəbul edib service-ə ötürür. Lesson 4-də üzərinə
 * @RestController / @PostMapping annotasiyaları əlavə ediləcək və eyni
 * struktur real REST endpoint-lərinə çevriləcək.
 */
public class UserController {

    private final UserService userService;
    private final TaskService taskService;

    public UserController(UserService userService, TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    // POST /users
    public UserResponse create(CreateUserRequest request) {
        return userService.createUser(request);
    }

    // GET /users/{id}
    public UserResponse getById(Long id) {
        return userService.getUserById(id);
    }

    // GET /users
    public List<UserResponse> getAll() {
        return userService.getAllUsers();
    }

    // PUT /users/{id}
    public UserResponse update(Long id, UpdateUserRequest request) {
        return userService.updateUser(id, request);
    }

    // GET /users/{id}/tasks
    public List<TaskResponse> getUserTasks(Long id) {
        return taskService.getTasksByUser(id);
    }

    // DELETE /users/{id}
    public void delete(Long id) {
        userService.deleteUser(id);
    }
}
