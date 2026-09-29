package az.training.taskmanagement.service;

import az.training.taskmanagement.exception.CategoryNotFoundException;
import az.training.taskmanagement.exception.TaskNotFoundException;
import az.training.taskmanagement.exception.UserNotFoundException;
import az.training.taskmanagement.exception.ValidationException;
import az.training.taskmanagement.model.Category;
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
 *
 * Task yaradılarkən user-in və (verilibsə) category-nin mövcudluğu yoxlanılır.
 */
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public TaskService(TaskRepository taskRepository,
                       UserRepository userRepository,
                       CategoryRepository categoryRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public Task createTask(String title, String description, Priority priority,
                           Long userId, Long categoryId) {
        if (title == null || title.isBlank()) {
            throw new ValidationException("title boş ola bilməz");
        }
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException(userId);
        }
        validateCategory(categoryId);

        Task task = new Task(null, title, description,
                TaskStatus.TODO, priority == null ? Priority.MEDIUM : priority,
                userId, categoryId);
        return taskRepository.save(task);
    }

    /**
     * categoryId məcburi deyil (null ola bilər).
     * Verilibsə: category mövcud və aktiv olmalıdır.
     */
    private void validateCategory(Long categoryId) {
        if (categoryId == null) {
            return;
        }
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        if (!category.isActive()) {
            throw new ValidationException("Category aktiv deyil: id=" + categoryId);
        }
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

    public List<Task> getTasksByStatus(TaskStatus status) {
        if (status == null) {
            throw new ValidationException("status boş ola bilməz");
        }
        return taskRepository.findByStatus(status);
    }

    public Task updateStatus(Long id, TaskStatus status) {
        if (status == null) {
            throw new ValidationException("status boş ola bilməz");
        }
        Task task = getTaskById(id);
        task.setStatus(status);
        task.setUpdatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        getTaskById(id); // mövcudluğu yoxla
        taskRepository.deleteById(id);
    }
}