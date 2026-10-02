package az.training.taskmanagement.service;

import az.training.taskmanagement.exception.CategoryNotFoundException;
import az.training.taskmanagement.exception.TaskNotFoundException;
import az.training.taskmanagement.exception.UserNotFoundException;
import az.training.taskmanagement.model.Priority;
import az.training.taskmanagement.model.Task;
import az.training.taskmanagement.model.TaskStatus;
import az.training.taskmanagement.repository.CategoryRepository;
import az.training.taskmanagement.repository.TaskRepository;
import az.training.taskmanagement.repository.UserRepository;
import java.util.List;

import static az.training.taskmanagement.util.ValidationUtils.requireNonBlank;
import static az.training.taskmanagement.util.ValidationUtils.requireNonNull;
/**
 * Task üçün business logic.
 *
 * Task yaradılarkən əvvəlcə user-in mövcudluğu yoxlanılır -
 * bu, iki service/repository arasında əlaqənin nümunəsidir.
 */
public class TaskService {

    private static final int TITLE_MAX_LENGTH = 200;

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
        String validTitle = requireNonBlank(title, "title", TITLE_MAX_LENGTH);
        ensureUserExists(userId);
        ensureCategoryExistsIfPresent(categoryId);

        Task task = Task.builder()
                .title(validTitle)
                .description(description)
                .priority(priority)
                .userId(userId)
                .categoryId(categoryId)
                .build();
        return taskRepository.save(task);
    }

    public Task createTask(String title, String description, Priority priority, Long userId) {
        return createTask(title, description, priority, userId, null);
    }

    public Task getTaskById(Long id) {
        requireNonNull(id, "id");
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getTasksByUser(Long userId) {
        ensureUserExists(userId);   // mövcud olmayan user üçün boş siyahı yox, xəta
        return taskRepository.findByUserId(userId);
    }

    public List<Task> getTasksByStatus(TaskStatus status) {
        requireNonNull(status, "status");
        return taskRepository.findByStatus(status);
    }

    public List<Task> getTasksByCategory(Long categoryId) {
        requireNonNull(categoryId, "categoryId");
        ensureCategoryExistsIfPresent(categoryId);
        return taskRepository.findByCategoryId(categoryId);
    }

    public Task updateStatus(Long id, TaskStatus status) {
        requireNonNull(status, "status");
        Task task = getTaskById(id);
        if (task.getStatus() == status) {
            return task;
        }
        task.setStatus(status);
        task.touch();
        return taskRepository.save(task);
    }

    /** Task-ı category-yə bağlayır; categoryId = null category-ni ayırır. */
    public Task assignCategory(Long taskId, Long categoryId) {
        Task task = getTaskById(taskId);
        ensureCategoryExistsIfPresent(categoryId);
        task.setCategoryId(categoryId);
        task.touch();
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        getTaskById(id); // mövcudluğu yoxla
        taskRepository.deleteById(id);
    }

    private void ensureUserExists(Long userId) {
        requireNonNull(userId, "userId");
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
    }

    private void ensureCategoryExistsIfPresent(Long categoryId) {
        if (categoryId != null && !categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException(categoryId);
        }
    }

}
