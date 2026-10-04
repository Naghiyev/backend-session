package az.training.taskmanagement.controller;

import az.training.taskmanagement.dto.task.request.CreateTaskRequest;
import az.training.taskmanagement.dto.task.response.TaskResponse;
import az.training.taskmanagement.dto.task.request.UpdateTaskRequest;
import az.training.taskmanagement.service.TaskService;

import java.util.List;

public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // POST /tasks
    public TaskResponse create(CreateTaskRequest request) {
        return taskService.createTask(request);
    }

    // GET /tasks
    public List<TaskResponse> getAll() {
        return taskService.getAllTasks();
    }

    // GET /tasks/{id}
    public TaskResponse getById(Long id) {
        return taskService.getTaskById(id);
    }

    // PATCH /tasks/{id}
    public TaskResponse update(Long id, UpdateTaskRequest request) {
        return taskService.updateTask(id, request);
    }

    // DELETE /tasks/{id}
    public void delete(Long id) {
        taskService.deleteTask(id);
    }
}
