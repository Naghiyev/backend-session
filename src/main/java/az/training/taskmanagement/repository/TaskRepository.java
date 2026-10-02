package az.training.taskmanagement.repository;

import az.training.taskmanagement.model.Task;
import az.training.taskmanagement.model.TaskStatus;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

/**
 * In-memory Task repository.
 */
public class TaskRepository {

    private final Map<Long, Task> storage = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public Task save(Task task) {
        if (task.getId() == null) {
            task.setId(sequence.incrementAndGet());
        }
        storage.put(task.getId(), task);
        return task;
    }

    public Optional<Task> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Task> findAll() {
        return findWhere(task -> true);
    }

    public List<Task> findByUserId(Long userId) {
        return findWhere(task -> Objects.equals(task.getUserId(), userId));
    }

    public List<Task> findByStatus(TaskStatus status) {
        return findWhere(task -> task.getStatus() == status);
    }

    public List<Task> findByCategoryId(Long categoryId) {
        return findWhere(task -> Objects.equals(task.getCategoryId(), categoryId));
    }

    public boolean existsByUserId(Long userId) {
        return storage.values().stream()
                .anyMatch(task -> Objects.equals(task.getUserId(), userId));
    }

    public boolean existsByCategoryId(Long categoryId) {
        return storage.values().stream()
                .anyMatch(task -> Objects.equals(task.getCategoryId(), categoryId));
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }

    private List<Task> findWhere(Predicate<Task> condition) {
        return storage.values().stream()
                .filter(condition)
                .sorted(Comparator.comparing(Task::getId))
                .toList();
    }
}