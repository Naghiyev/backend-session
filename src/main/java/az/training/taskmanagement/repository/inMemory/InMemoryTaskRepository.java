package az.training.taskmanagement.repository.inMemory;

import az.training.taskmanagement.model.Task;
import az.training.taskmanagement.model.TaskStatus;
import az.training.taskmanagement.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory Task repository.
 */
public class InMemoryTaskRepository implements TaskRepository {

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
        return new ArrayList<>(storage.values());
    }
    @Override
    public List<Task> findByUserId(Long userId) {
        List<Task> result = new ArrayList<>();
        for (Task task : storage.values()) {
            if (task.getUserId() != null && task.getUserId().equals(userId)) {
                result.add(task);
            }
        }
        return result;
    }
    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }

    @Override
    public List<Task> getTasksByStatus(TaskStatus status){
    	List<Task> result = new ArrayList<>();
    	for (Task task : storage.values()) {
			if (task.getStatus() == status) {
				result.add(task);
			}
		}
    	return result;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
