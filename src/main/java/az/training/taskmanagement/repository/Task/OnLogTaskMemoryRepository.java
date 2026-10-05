package az.training.taskmanagement.repository.Task;

import az.training.taskmanagement.model.Task;
import az.training.taskmanagement.repository.TaskRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class OnLogTaskMemoryRepository implements TaskRepository {
    private final Map<Long, Task> storage = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    @Override
    public Task save(Task task) {
        return null;
    }

    @Override
    public Optional<Task> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Task> findAll() {
        return List.of();
    }

    @Override
    public List<Task> findByUserId(Long userId) {
        return List.of();
    }

    @Override
    public void deleteById(Long id) {

    }
}
