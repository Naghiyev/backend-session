package az.training.taskmanagement.controller;

import az.training.taskmanagement.dto.CreateTaskRequest;
import az.training.taskmanagement.dto.TaskResponse;
import az.training.taskmanagement.dto.UpdateTaskRequest;
import az.training.taskmanagement.model.Priority;
import az.training.taskmanagement.model.TaskStatus;
import az.training.taskmanagement.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(@RequestBody CreateTaskRequest request) {
        TaskResponse created = taskService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // GET /tasks  və  GET /tasks?status=TODO
    @GetMapping
    public List<TaskResponse> getAll(@RequestParam(required = false) TaskStatus status) {
        return taskService.getTasks(status);
    }

    @GetMapping("/{id}")
    public TaskResponse getById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @GetMapping("/by-user/{userId}/priority")
    public List<TaskResponse> getByUserAndPriority(@PathVariable Long userId,
                                                   @RequestParam Priority priority) {
        return taskService.getUserTasksByPriority(userId, priority);
    }

    @GetMapping("/by-user/{userId}")
    public List<TaskResponse> getByUserAndStatus(@PathVariable Long userId,
                                                 @PathVariable TaskStatus taskstatus){
        return taskService.getTasksByUserAndStatus(userId, taskstatus);
    }

    @PatchMapping("/{id}")
    public TaskResponse update(@PathVariable Long id, @RequestBody UpdateTaskRequest request) {
        return taskService.updateTask(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
