package az.training.taskmanagement.service;

import az.training.taskmanagement.exception.CategoryNotFoundException;
import az.training.taskmanagement.exception.TaskNotFoundException;
import az.training.taskmanagement.exception.TaskValidationException;
import az.training.taskmanagement.exception.UserNotFoundException;
import az.training.taskmanagement.model.Priority;
import az.training.taskmanagement.model.Task;
import az.training.taskmanagement.model.TaskStatus;
import az.training.taskmanagement.repository.inMemory.InMemoryCategoryRepository;
import az.training.taskmanagement.repository.inMemory.InMemoryTaskRepository;
import az.training.taskmanagement.repository.inMemory.InMemoryUserRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Task üçün business logic.
 *
 * Task yaradılarkən əvvəlcə user-in mövcudluğu yoxlanılır -
 * bu, iki service/repository arasında əlaqənin nümunəsidir.
 */
public class TaskService {

    private final InMemoryTaskRepository taskRepository;
    private final InMemoryUserRepository userRepository;
    private final InMemoryCategoryRepository categoryRepository;

    public TaskService(InMemoryTaskRepository taskRepository, InMemoryUserRepository userRepository, InMemoryCategoryRepository categoryRepository) {

        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public Task createTask(String title, String description, Priority priority, Long userId , Long categoryId) {
        if (title == null || title.isBlank()) {
            throw new TaskValidationException("title boş ola bilməz");
        }
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("User tapılmadı: id=" + userId);
        }
        if (categoryRepository.findById(categoryId).isEmpty()) {
            throw new CategoryNotFoundException("Category tapılmadı: id=" + categoryId);
        }
        Task task = new Task(null, title, description,
                TaskStatus.TODO, priority == null ? Priority.MEDIUM : priority, userId , categoryId);
        return taskRepository.save(task);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task tapılmadı: id=" + id));
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getTasksByUser(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    public Task updateStatus(Long id, TaskStatus status) {
        Task task = getTaskById(id);
        task.setStatus(status);
        task.setUpdatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        getTaskById(id); // mövcudluğu yoxla
        taskRepository.deleteById(id);
    }

    public List<Task> getTasksByStatus(TaskStatus status){
    	return taskRepository.getTasksByStatus(status);
    }

}
