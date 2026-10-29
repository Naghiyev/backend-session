package az.training.taskmanagement.service;

import az.training.taskmanagement.dto.CreateTaskRequest;
import az.training.taskmanagement.dto.TaskResponse;
import az.training.taskmanagement.dto.UpdateTaskRequest;
import az.training.taskmanagement.model.Priority;
import az.training.taskmanagement.model.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {

    TaskResponse createTask(CreateTaskRequest request);

    TaskResponse getTaskById(Long id);

//    Page<TaskResponse> getAllTasks(Pageable pageable);
//
//    /** status null olduqda bütün task-lar, əks halda yalnız həmin statusdakılar. */
//    List<TaskResponse> getTasks(TaskStatus status, Priority priority);


    Page<TaskResponse> getTasks(
            TaskStatus status,
            Priority priority,
            Pageable pageable);

    List<TaskResponse> getTasksByUser(Long userId);

    TaskResponse updateTask(Long id, UpdateTaskRequest request);

    TaskResponse replaceTask(Long id, UpdateTaskRequest request);

    void deleteTask(Long id);
}
