package az.training.taskmanagement.service;

import az.training.taskmanagement.dto.task.request.CreateTaskRequest;
import az.training.taskmanagement.dto.task.request.UpdateTaskRequest;
import az.training.taskmanagement.dto.task.response.TaskResponse;
import az.training.taskmanagement.exception.ResourceNotFoundException;
import az.training.taskmanagement.exception.ValidationException;
import az.training.taskmanagement.mapper.TaskMapper;
import az.training.taskmanagement.model.Category;
import az.training.taskmanagement.model.Task;
import az.training.taskmanagement.model.User;
import az.training.taskmanagement.repository.CategoryRepository;
import az.training.taskmanagement.repository.TaskRepository;
import az.training.taskmanagement.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository,
                       CategoryRepository categoryRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public TaskResponse createTask(CreateTaskRequest request) {
        if (request.title() == null || request.title().isBlank()) {
            throw new ValidationException("title boş ola bilməz");
        }
        if (request.userId() == null) {
            throw new ValidationException("userId boş ola bilməz");
        }
        findUserOrThrow(request.userId());

        if (request.categoryId() != null) {
            findCategoryOrThrow(request.categoryId());
        }
        Task saved = taskRepository.save(TaskMapper.toEntity(request));
        return TaskMapper.toResponse(saved);
    }

    public TaskResponse getTaskById(Long id) {
        return TaskMapper.toResponse(findTaskOrThrow(id));
    }

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(TaskMapper::toResponse)
                .toList();
    }

    public List<TaskResponse> getTasksByUser(Long userId) {
        findUserOrThrow(userId);
        return taskRepository.findByUserId(userId).stream()
                .map(TaskMapper::toResponse)
                .toList();
    }

    public List<TaskResponse> getTasksByCategory(Long categoryId) {
        findCategoryOrThrow(categoryId);
        return taskRepository.findByCategoryId(categoryId).stream()
                .map(TaskMapper::toResponse)
                .toList();
    }

    public TaskResponse updateTask(Long id, UpdateTaskRequest request) {
        Task task = findTaskOrThrow(id);
        if (request.title() != null) {
            task.setTitle(request.title());
        }
        if (request.description() != null) {
            task.setDescription(request.description());
        }
        if (request.status() != null) {
            task.setStatus(request.status());
        }
        if (request.priority() != null) {
            task.setPriority(request.priority());
        }
        task.setUpdatedAt(LocalDateTime.now());
        return TaskMapper.toResponse(taskRepository.save(task));
    }

    public void deleteTask(Long id) {
        findTaskOrThrow(id);
        taskRepository.deleteById(id);
    }

    private Task findTaskOrThrow(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Task", id));
    }

    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("User", id));
    }

    private Category findCategoryOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Category", id));
    }
}