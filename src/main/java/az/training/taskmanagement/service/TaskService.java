package az.training.taskmanagement.service;

import az.training.taskmanagement.dto.CreateTaskRequest;
import az.training.taskmanagement.dto.FullUpdateTaskRequest;
import az.training.taskmanagement.dto.TaskResponse;
import az.training.taskmanagement.dto.UpdateTaskRequest;
import az.training.taskmanagement.model.Priority;
import az.training.taskmanagement.model.TaskStatus;

import java.util.List;

public interface TaskService {

    TaskResponse createTask(CreateTaskRequest request);

    TaskResponse getTaskById(Long id);

    List<TaskResponse> getAllTasks();

    List<TaskResponse> getTasks(TaskStatus status, Priority priority);

    List<TaskResponse> getTasksByUser(Long userId);

    TaskResponse updateTask(Long id, UpdateTaskRequest request);

    TaskResponse fullUpdateTask(Long id, FullUpdateTaskRequest request);

    void deleteTask(Long id);
}
