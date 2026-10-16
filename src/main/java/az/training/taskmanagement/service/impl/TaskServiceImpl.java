package az.training.taskmanagement.service.impl;

import az.training.taskmanagement.dto.CreateTaskRequest;
import az.training.taskmanagement.dto.TaskResponse;
import az.training.taskmanagement.dto.UpdateTaskRequest;
import az.training.taskmanagement.exception.ResourceNotFoundException;
import az.training.taskmanagement.exception.ValidationException;
import az.training.taskmanagement.mapper.TaskMapper;
import az.training.taskmanagement.model.Category;
import az.training.taskmanagement.model.Task;
import az.training.taskmanagement.model.TaskStatus;
import az.training.taskmanagement.model.User;
import az.training.taskmanagement.repository.CategoryRepository;
import az.training.taskmanagement.repository.TaskRepository;
import az.training.taskmanagement.repository.UserRepository;
import az.training.taskmanagement.service.TaskService;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@org.springframework.stereotype.Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository, CategoryRepository categoryRepository) {

        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.categoryRepository= categoryRepository;
    }

    @Override
    @Transactional
    public TaskResponse createTask(CreateTaskRequest request) {
        if (request.title() == null || request.title().isBlank()) {
            throw new ValidationException("title boş ola bilməz");
        }
        if (request.userId() == null) {
            throw new ValidationException("userId boş ola bilməz");
        }
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> ResourceNotFoundException.of("User", request.userId()));

        Category category = null;
        if (request.categoryId() != null) {
            category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> ResourceNotFoundException.of("Category", request.categoryId()));
        }

        Task saved = taskRepository.save(TaskMapper.toEntity(request, user, category));
        return TaskMapper.toResponse(saved);
    }

    @Override
    public List<TaskResponse> getTasksByCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw ResourceNotFoundException.of("Category", categoryId);
        }
        return taskRepository.findByCategoryId(categoryId).stream()
                .map(TaskMapper::toResponse)
                .toList();
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
    public List<TaskResponse> getTasks(TaskStatus status) {
        return taskRepository.findAll().stream()
                .filter(t -> status == null || t.getStatus() == status)
                .map(TaskMapper::toResponse)
                .toList();
    }

    @Override
    public List<TaskResponse> getTasksByUser(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw ResourceNotFoundException.of("User", userId);
        }
        return taskRepository.findByUserId(userId).stream()
                .map(TaskMapper::toResponse)
                .toList();
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
}
