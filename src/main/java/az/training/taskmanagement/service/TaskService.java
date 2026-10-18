package az.training.taskmanagement.service;

import az.training.taskmanagement.dto.CreateTaskRequest;
import az.training.taskmanagement.dto.TaskResponse;
import az.training.taskmanagement.dto.UpdateTaskRequest;
import az.training.taskmanagement.model.Priority;
import az.training.taskmanagement.model.Task;
import az.training.taskmanagement.model.TaskStatus;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskService {

    TaskResponse createTask(CreateTaskRequest request);

    TaskResponse getTaskById(Long id);

    List<TaskResponse> getAllTasks();

    /** status null olduqda bütün task-lar, əks halda yalnız həmin statusdakılar. */
    List<TaskResponse> getTasks(TaskStatus status);

    List<TaskResponse> getTasksByUser(Long userId);

    List<TaskResponse> getByUser_IdAndStatus(Long userId, TaskStatus status);

    List<TaskResponse> getTasksByStatusAndPriority(TaskStatus status, Priority priority);

    TaskResponse updateTask(Long id, UpdateTaskRequest request);

    void deleteTask(Long id);
}
