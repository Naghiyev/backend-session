package az.training.taskmanagement.repository;

import az.training.taskmanagement.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    Task save(Task task);

    Optional<Task> findById(Long id);

    List<Task> findAll();

    List<Task> findByUserId(Long userId);

    List<Task> findByCategoryId(Long categoryId);

    void deleteById(Long id);
}
