package az.training.taskmanagement.controller;

import az.training.taskmanagement.dto.CreateTaskRequest;
import az.training.taskmanagement.dto.TaskResponse;
import az.training.taskmanagement.model.TaskStatus;
import az.training.taskmanagement.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Lesson 5 — fundamental endpoints only.
 * Status filter, PATCH və DELETE Lesson 6-da gəlir.
 */
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(@RequestBody CreateTaskRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(request));
    }

    @GetMapping
    public List<TaskResponse> getAll(@RequestParam(required = false) TaskStatus taskStatus) {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public TaskResponse getById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }
}
