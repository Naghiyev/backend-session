package az.training.taskmanagement.service.impl;

import az.training.taskmanagement.dto.CreateTaskRequest;
import az.training.taskmanagement.dto.TaskResponse;
import az.training.taskmanagement.dto.UpdateTaskRequest;
import az.training.taskmanagement.exception.ResourceNotFoundException;
import az.training.taskmanagement.exception.ValidationException;
import az.training.taskmanagement.mapper.TaskMapper;
import az.training.taskmanagement.model.Category;
import az.training.taskmanagement.model.Task;
import az.training.taskmanagement.model.User;
import az.training.taskmanagement.repository.CategoryRepository;
import az.training.taskmanagement.repository.TaskRepository;
import az.training.taskmanagement.repository.UserRepository;
import az.training.taskmanagement.service.NotificationService;
import az.training.taskmanagement.service.TaskService;

import java.time.LocalDateTime;
import java.util.List;

public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final NotificationService notificationService;

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository,CategoryRepository categoryRepository, NotificationService notificationService) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.notificationService = notificationService;
    }

    @Override
    public TaskResponse createTask(CreateTaskRequest request) {
        if (request.title() == null || request.title().isBlank()) {
            throw new ValidationException("title boş ola bilməz");
        }
        // Task yaratmazdan əvvəl user-in mövcudluğunu yoxla.
//        if (userRepository.findById(request.userId()).isEmpty()) {
//            throw ResourceNotFoundException.of("User", request.userId());
//        }
        findUserOrThrow(request.userId());
        Task saved = taskRepository.save(TaskMapper.toEntity(request));
        notificationService.notifyTaskCreated(saved);
        return TaskMapper.toResponse(saved);
    }

    @Override
    public TaskResponse getTaskById(Long id) {
        return TaskMapper.toResponse(findTaskOrThrow(id));
    }

    @Override
    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(TaskMapper::toResponse)
                .toList();
    }

    @Override
    public List<TaskResponse> getTasksByUser(Long userId) {
//        if (userRepository.findById(userId).isEmpty()) {
//            throw ResourceNotFoundException.of("User", userId);
//        }
        findUserOrThrow(userId);
        return taskRepository.findByUserId(userId).stream()
                .map(TaskMapper::toResponse)
                .toList();
    }
    @Override
    public List<TaskResponse> getTasksByCategory(Long categoryId){
        findCategoryOrThrow(categoryId);
        return taskRepository.findByCategoryId(categoryId).stream()
                .map(TaskMapper :: toResponse).toList();
    }

    @Override
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

    @Override
    public void deleteTask(Long id) {
        findTaskOrThrow(id);
        taskRepository.deleteById(id);
    }

    private Task findTaskOrThrow(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Task", id));
    }

    private User findUserOrThrow(Long id){
        return userRepository.findById(id).orElseThrow(()-> ResourceNotFoundException.of("User",id));
    }
    private Category findCategoryOrThrow(Long id){
        return categoryRepository.findById(id).orElseThrow(()->ResourceNotFoundException.of("Category",id));
    }
}
