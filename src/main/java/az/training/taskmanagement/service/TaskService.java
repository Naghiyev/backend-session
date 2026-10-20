package az.training.taskmanagement.service;

import az.training.taskmanagement.dto.CreateTaskRequest;
import az.training.taskmanagement.dto.TaskResponse;
import az.training.taskmanagement.dto.UpdateTaskRequest;
import az.training.taskmanagement.model.Task;
import az.training.taskmanagement.model.TaskStatus;

import java.util.List;

public interface TaskService {

    TaskResponse createTask(CreateTaskRequest request);

    TaskResponse getTaskById(Long id);

    List<TaskResponse> getAllTasks();

    List<TaskResponse> searchTask(String keyword);

    List<TaskResponse> getTasks(TaskStatus status);

    List<TaskResponse> getTasksByUser(Long userId);

    List<TaskResponse> findByUserIdAndStatus(Long userId, TaskStatus status);

    TaskResponse updateTask(Long id, UpdateTaskRequest request);

    void deleteTask(Long id);


}
