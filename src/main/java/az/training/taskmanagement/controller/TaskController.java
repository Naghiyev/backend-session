package az.training.taskmanagement.controller;

import az.training.taskmanagement.dto.*;
import az.training.taskmanagement.model.Priority;
import az.training.taskmanagement.model.TaskStatus;
import az.training.taskmanagement.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
//    @GetMapping
//    public List<TaskResponse> getAllOrStatusOrPriority(@RequestParam(required = false) TaskStatus status, @RequestParam(required = false) Priority priority) {
//        return taskService.getTasks(status,priority);
//    }
//    @GetMapping
//    public Page<TaskResponse> getAll(@PageableDefault(size = 10, sort = "id") Pageable pageable){
//        return taskService.getAllTasks(pageable);
//    }

    @GetMapping
    public Page<TaskResponse> getTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) Priority priority,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {

        return taskService.getTasks(status, priority, pageable);
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
    public TaskResponse replaceTask(Long id, UpdateTaskRequest request){
        return taskService.replaceTask(id,request);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
