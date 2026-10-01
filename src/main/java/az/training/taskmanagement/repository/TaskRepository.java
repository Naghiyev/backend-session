package az.training.taskmanagement.repository;

import az.training.taskmanagement.model.Task;
import az.training.taskmanagement.model.TaskStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

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
        return new ArrayList<>(storage.values());
    }

    public List<Task> findByUserId(Long userId) {
        List<Task> result = new ArrayList<>();
        for (Task task : storage.values()) {
            if (task.getUserId() != null && task.getUserId().equals(userId)) {
                result.add(task);
            }
        }
        return result;
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }

    public List<Task> findByStatus(TaskStatus status){
        List<Task> result = new ArrayList<>();
        for (Task task : storage.values()) {
            if (task.getStatus().equals(status)) {
                result.add(task);
            }
        }
        return result;
    }
}
