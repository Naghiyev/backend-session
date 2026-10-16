package az.training.taskmanagement.repository;

import az.training.taskmanagement.model.Task;
import az.training.taskmanagement.model.TaskStatus;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    Task save(Task task);

    Optional<Task> findById(Long id);

    List<Task> findAll();

    List<Task> findByUserId(Long userId);

    List<Task> getTasksByStatus(TaskStatus status);

    void deleteById(Long id);
}
