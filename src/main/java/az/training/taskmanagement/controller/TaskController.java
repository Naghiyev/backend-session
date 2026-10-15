package az.training.taskmanagement.controller;

import az.training.taskmanagement.dto.CreateTaskRequest;
import az.training.taskmanagement.dto.FullUpdateTaskRequest;
import az.training.taskmanagement.dto.TaskResponse;
import az.training.taskmanagement.dto.UpdateTaskRequest;
import az.training.taskmanagement.model.Priority;
import az.training.taskmanagement.model.TaskStatus;
import az.training.taskmanagement.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping
    public List<TaskResponse> getAll(@RequestParam(required = false) TaskStatus status,
                                     @RequestParam(required = false) Priority priority) {
        return taskService.getTasks(status, priority);
    }

    @GetMapping("/{id}")
    public TaskResponse getById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PatchMapping("/{id}")
    public TaskResponse update(@PathVariable Long id, @RequestBody UpdateTaskRequest request) {
        return taskService.updateTask(id, request);
    }

    @PutMapping("/{id}")
    public TaskResponse replace(@PathVariable Long id, @RequestBody FullUpdateTaskRequest request) {
        return taskService.fullUpdateTask(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
