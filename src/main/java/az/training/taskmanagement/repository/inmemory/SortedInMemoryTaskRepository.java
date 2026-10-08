package az.training.taskmanagement.repository.inmemory;

import az.training.taskmanagement.model.Task;
import az.training.taskmanagement.repository.TaskRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class SortedInMemoryTaskRepository implements TaskRepository {

    private final Map<Long, Task> storage = new TreeMap<>();
    private final AtomicLong sequence = new AtomicLong();

    @Override
    public Task save(Task task) {
        if (task.getId() == null) {
            long id = sequence.incrementAndGet();
            task.setId(id);
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
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Task> findByUserId(Long userId) {
        return storage.values()
                .stream()
                .filter(task -> task.getUserId().equals(userId))
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }
}
