package az.training.taskmanagement.service.impl;

import az.training.taskmanagement.dto.CreateTaskRequest;
import az.training.taskmanagement.dto.FullUpdateTaskRequest;
import az.training.taskmanagement.dto.PageResponse;
import az.training.taskmanagement.dto.TaskResponse;
import az.training.taskmanagement.dto.UpdateTaskRequest;
import az.training.taskmanagement.exception.ResourceNotFoundException;
import az.training.taskmanagement.exception.ValidationException;
import az.training.taskmanagement.mapper.TaskMapper;
import az.training.taskmanagement.model.Priority;
import az.training.taskmanagement.model.Task;
import az.training.taskmanagement.model.TaskStatus;
import az.training.taskmanagement.repository.TaskRepository;
import az.training.taskmanagement.repository.UserRepository;
import az.training.taskmanagement.service.TaskService;
import az.training.taskmanagement.util.PageUtils;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@org.springframework.stereotype.Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public TaskResponse createTask(CreateTaskRequest request) {
        if (request.title() == null || request.title().isBlank()) {
            throw new ValidationException("title boş ola bilməz");
        }
        // Task yaratmazdan əvvəl user-in mövcudluğunu yoxla.
        if (userRepository.findById(request.userId()).isEmpty()) {
            throw ResourceNotFoundException.of("User", request.userId());
        }
        Task saved = taskRepository.save(TaskMapper.toEntity(request));
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
    public PageResponse<TaskResponse> getTasks(TaskStatus status, Priority priority, int page, int size) {
        List<TaskResponse> filtered = taskRepository.findAll().stream()
                .filter(t -> status == null || t.getStatus() == status)
                .filter(t -> priority == null || t.getPriority() == priority)
                .map(TaskMapper::toResponse)
                .toList();
        return PageUtils.paginate(filtered, page, size);
    }

    @Override
    public PageResponse<TaskResponse> getTasksByUser(Long userId, int page, int size) {
        if (userRepository.findById(userId).isEmpty()) {
            throw ResourceNotFoundException.of("User", userId);
        }
        List<TaskResponse> tasks = taskRepository.findByUserId(userId).stream()
                .sorted(Comparator.comparing(Task::getId))
                .map(TaskMapper::toResponse)
                .toList();
        return PageUtils.paginate(tasks, page, size);
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
    public TaskResponse fullUpdateTask(Long id, FullUpdateTaskRequest request) {
        Task task = findTaskOrThrow(id);

        if (request.title() == null || request.title().isBlank()) {
            throw new ValidationException("title boş ola bilməz");
        }
        if (request.status() == null) {
            throw new ValidationException("status boş ola bilməz");
        }
        if (request.priority() == null) {
            throw new ValidationException("priority boş ola bilməz");
        }

        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(request.status());
        task.setPriority(request.priority());
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
}
