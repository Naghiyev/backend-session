package az.training.taskmanagement.service;

import az.training.taskmanagement.exception.CategoryNotFoundException;
import az.training.taskmanagement.exception.TaskNotFoundException;
import az.training.taskmanagement.exception.UserNotFoundException;
import az.training.taskmanagement.exception.ValidationException;
import az.training.taskmanagement.model.Priority;
import az.training.taskmanagement.model.Task;
import az.training.taskmanagement.model.TaskStatus;
import az.training.taskmanagement.repository.CategoryRepository;
import az.training.taskmanagement.repository.TaskRepository;
import az.training.taskmanagement.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Task üçün business logic.
 * Task yaradılarkən əvvəlcə user-in mövcudluğu yoxlanılır -
 * bu, iki service/repository arasında əlaqənin nümunəsidir.
 */
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public Task createTask(String title, String description, Priority priority, Long userId, Long categoryId) {
        if (title == null || title.isBlank()) {
            throw new ValidationException("title boş ola bilməz");

        }
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException(userId);
        }
        if (categoryId != null && categoryRepository.findById(categoryId).isEmpty()) {
            throw new CategoryNotFoundException(categoryId);
        }
        Task task = new Task(null, title, description,
                TaskStatus.TODO, categoryId, priority == null ? Priority.MEDIUM : priority, userId);
        return taskRepository.save(task);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
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
    public Task updateCategory(Long id, Long categoryId) {
        validateCategory(categoryId);
        Task task = getTaskById(id);
        task.setCategoryId(categoryId);
        task.setUpdatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }
    public void deleteTask(Long id) {
        getTaskById(id); // mövcudluğu yoxla
        taskRepository.deleteById(id);
    }
    private void validateCategory(Long categoryId) {
        if (categoryId == null || !categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException(categoryId);
        }
    }

    public List<Task> getTasksByStatus(TaskStatus status) {
        if (status == null) {
            throw new ValidationException("Status null ola bilməz");
        }
        return taskRepository.findByStatus(status);
    }
}
