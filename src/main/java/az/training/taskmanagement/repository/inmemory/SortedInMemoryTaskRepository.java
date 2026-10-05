package az.training.taskmanagement.repository.inmemory;

import az.training.taskmanagement.model.Task;
import az.training.taskmanagement.model.User;
import az.training.taskmanagement.repository.TaskRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class SortedInMemoryTaskRepository implements TaskRepository {
    private final Map<Long, Task> storage = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    @Override
    public Task save(Task task) {
        if (task.getId() == null) {
            task.setId(sequence.incrementAndGet());
        }
        storage.put(task.getId(), task);
        return task;
    }

    @Override
    public Optional<Task> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Task> findAll() {
        return storage.values().stream()
                .sorted(Comparator.comparing(Task::getId))
                .toList();

    }

    @Override
    public List<Task> findByUserId(Long userId) {
        return storage.values().stream()
                .filter(task -> task.getUserId() != null && task.getUserId().equals(userId))//Objects.equals()
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }
}
