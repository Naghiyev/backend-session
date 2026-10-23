package az.training.taskmanagement.service.impl;

import az.training.taskmanagement.dto.CreateTaskRequest;
import az.training.taskmanagement.dto.TaskResponse;
import az.training.taskmanagement.dto.UpdateTaskRequest;
import az.training.taskmanagement.exception.ResourceNotFoundException;
import az.training.taskmanagement.exception.ValidationException;
import az.training.taskmanagement.mapper.TaskMapper;
import az.training.taskmanagement.model.Task;
import az.training.taskmanagement.model.TaskStatus;
import az.training.taskmanagement.model.User;
import az.training.taskmanagement.outbox.OutboxWriter;
import az.training.taskmanagement.outbox.event.TaskCreatedEvent;
import az.training.taskmanagement.outbox.event.TaskStatusChangedEvent;
import az.training.taskmanagement.repository.TaskRepository;
import az.training.taskmanagement.repository.UserRepository;
import az.training.taskmanagement.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final OutboxWriter outboxWriter;

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository, OutboxWriter outboxWriter) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.outboxWriter = outboxWriter;
    }

    @Override
    public TaskResponse createTask(CreateTaskRequest request) {
        if (request.title() == null || request.title().isBlank()) {
            throw new ValidationException("title boş ola bilməz");
        }
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> ResourceNotFoundException.of("User", request.userId()));
        Task saved = taskRepository.save(TaskMapper.toEntity(request, user));

        // Transactional Outbox: event Kafka-ya birbaşa yox, eyni tranzaksiyada outbox cədvəlinə yazılır.
        // Task INSERT və outbox INSERT birlikdə commit olunur (və ya birlikdə rollback).
        outboxWriter.append("task", String.valueOf(saved.getId()),
                TaskCreatedEvent.TYPE, TaskCreatedEvent.from(saved));

        log.info("Task yaradıldı: id={}, userId={}", saved.getId(), user.getId());
        return TaskMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "tasks", key = "#id")
    public TaskResponse getTaskById(Long id) {
        return TaskMapper.toResponse(findTaskOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(TaskMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getTasks(TaskStatus status) {
        List<Task> tasks = (status == null)
                ? taskRepository.findAll()
                : taskRepository.findByStatus(status);
        return tasks.stream().map(TaskMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw ResourceNotFoundException.of("User", userId);
        }
        return taskRepository.findByUser_Id(userId).stream()
                .map(TaskMapper::toResponse)
                .toList();
    }

    @Override
    @CacheEvict(value = "tasks", key = "#id")
    public TaskResponse updateTask(Long id, UpdateTaskRequest request) {
        Task task = findTaskOrThrow(id);
        TaskStatus oldStatus = task.getStatus();
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
        // updatedAt @PreUpdate ilə avtomatik yenilənir
        Task saved = taskRepository.save(task);

        // Status həqiqətən dəyişibsə, eyni tranzaksiyada TaskStatusChanged eventi yazılır
        if (saved.getStatus() != oldStatus) {
            outboxWriter.append("task", String.valueOf(saved.getId()),
                    TaskStatusChangedEvent.TYPE, TaskStatusChangedEvent.of(saved, oldStatus));
        }
        return TaskMapper.toResponse(saved);
    }

    @Override
    @CacheEvict(value = "tasks", key = "#id")
    public void deleteTask(Long id) {
        findTaskOrThrow(id);
        taskRepository.deleteById(id);
        log.info("Task silindi: id={}", id);
    }

    private Task findTaskOrThrow(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Task", id));
    }
}
