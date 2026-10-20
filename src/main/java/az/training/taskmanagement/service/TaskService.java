package az.training.taskmanagement.service;

import az.training.taskmanagement.exceptions.InvalidInputException;
import az.training.taskmanagement.exceptions.ResourceNotFoundException;
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
            throw new InvalidInputException("title bos ola bilmez");
        }
        if (userRepository.findById(userId).isEmpty()) {
            throw new ResourceNotFoundException("User",userId);
        }
        if (categoryId != null && categoryRepository.findById(categoryId).isEmpty()) {
            throw new ResourceNotFoundException("Category", categoryId);
        }
        Task task = new Task(null, title, description,
                TaskStatus.TODO, priority == null ? Priority.MEDIUM : priority, userId,categoryId);
        return taskRepository.save(task);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", id));
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


    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByTaskStatus(status);
    }
}

