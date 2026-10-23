package az.training.taskmanagement.service;

import az.training.taskmanagement.dto.CreateTaskRequest;
import az.training.taskmanagement.dto.TaskResponse;
import az.training.taskmanagement.dto.UpdateTaskRequest;
import az.training.taskmanagement.exception.ResourceNotFoundException;
import az.training.taskmanagement.exception.ValidationException;
import az.training.taskmanagement.model.Priority;
import az.training.taskmanagement.model.Task;
import az.training.taskmanagement.model.TaskStatus;
import az.training.taskmanagement.model.User;
import az.training.taskmanagement.outbox.OutboxWriter;
import az.training.taskmanagement.outbox.event.TaskCreatedEvent;
import az.training.taskmanagement.outbox.event.TaskStatusChangedEvent;
import az.training.taskmanagement.repository.TaskRepository;
import az.training.taskmanagement.repository.UserRepository;
import az.training.taskmanagement.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OutboxWriter outboxWriter;

    @InjectMocks
    private TaskServiceImpl taskService;

    private User existingUser() {
        User user = new User("Darya", "darya@example.com");
        user.setId(1L);
        return user;
    }

    @Test
    @DisplayName("createTask: mövcud user → task yaradılır")
    void createTask_success() {
        CreateTaskRequest request = new CreateTaskRequest("Backend task", "desc", Priority.HIGH, 1L);
        User user = existingUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Task saved = new Task("Backend task", "desc", TaskStatus.TODO, Priority.HIGH, user);
        saved.setId(10L);
        when(taskRepository.save(any())).thenReturn(saved);

        TaskResponse response = taskService.createTask(request);

        assertEquals(10L, response.id().longValue());
        assertEquals(TaskStatus.TODO, response.status());
        assertEquals(1L, response.userId().longValue());
        verify(taskRepository).save(any());
        // Outbox: TaskCreated eventi eyni tranzaksiyada yazılmalıdır
        verify(outboxWriter).append(eq("task"), eq("10"), eq(TaskCreatedEvent.TYPE), any(TaskCreatedEvent.class));
    }

    @Test
    @DisplayName("createTask: user tapılmır → ResourceNotFoundException")
    void createTask_userNotFound_throws() {
        CreateTaskRequest request = new CreateTaskRequest("Task", null, Priority.LOW, 5L);
        when(userRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.createTask(request));
        verify(taskRepository, never()).save(any());
        verifyNoInteractions(outboxWriter);
    }

    @Test
    @DisplayName("createTask: boş title → ValidationException")
    void createTask_blankTitle_throws() {
        CreateTaskRequest request = new CreateTaskRequest("", "desc", Priority.LOW, 1L);
        assertThrows(ValidationException.class, () -> taskService.createTask(request));
    }

    @Test
    @DisplayName("getTaskById: tapılmır → ResourceNotFoundException")
    void getTaskById_notFound_throws() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> taskService.getTaskById(99L));
    }

    @Test
    @DisplayName("updateTask: status dəyişir")
    void updateTask_changesStatus() {
        User user = existingUser();
        Task task = new Task("Task", "desc", TaskStatus.TODO, Priority.MEDIUM, user);
        task.setId(3L);
        when(taskRepository.findById(3L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any())).thenReturn(task);

        UpdateTaskRequest request = new UpdateTaskRequest(null, null, TaskStatus.DONE, null);
        TaskResponse response = taskService.updateTask(3L, request);

        assertEquals(TaskStatus.DONE, response.status());
        verify(outboxWriter).append(eq("task"), eq("3"), eq(TaskStatusChangedEvent.TYPE), any(TaskStatusChangedEvent.class));
    }

    @Test
    @DisplayName("updateTask: status dəyişmirsə → outbox event yazılmır")
    void updateTask_withoutStatusChange_noOutboxEvent() {
        User user = existingUser();
        Task task = new Task("Task", "desc", TaskStatus.TODO, Priority.MEDIUM, user);
        task.setId(4L);
        when(taskRepository.findById(4L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any())).thenReturn(task);

        taskService.updateTask(4L, new UpdateTaskRequest("Yeni ad", null, null, null));

        verifyNoInteractions(outboxWriter);
    }

    @Test
    @DisplayName("getTasksByUser: user yoxdur → ResourceNotFoundException")
    void getTasksByUser_userMissing_throws() {
        when(userRepository.existsById(9L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> taskService.getTasksByUser(9L));
    }
}
