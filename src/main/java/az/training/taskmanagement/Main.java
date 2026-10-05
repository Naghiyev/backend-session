package az.training.taskmanagement;

import az.training.taskmanagement.controller.TaskController;
import az.training.taskmanagement.controller.UserController;
import az.training.taskmanagement.dto.*;
import az.training.taskmanagement.exception.DuplicateResourceException;
import az.training.taskmanagement.model.Priority;
import az.training.taskmanagement.model.TaskStatus;
import az.training.taskmanagement.repository.TaskRepository;
import az.training.taskmanagement.repository.UserRepository;
import az.training.taskmanagement.repository.inmemory.InMemoryTaskRepository;
import az.training.taskmanagement.repository.inmemory.InMemoryUserRepository;
import az.training.taskmanagement.repository.inmemory.SortedInMemoryTaskRepository;
import az.training.taskmanagement.service.NotificationService;
import az.training.taskmanagement.service.TaskService;
import az.training.taskmanagement.service.UserService;
import az.training.taskmanagement.service.impl.NoOpNotificationService;
import az.training.taskmanagement.service.impl.TaskServiceImpl;
import az.training.taskmanagement.service.impl.UserServiceImpl;

import java.util.List;

/**
 * Lesson 2 demo — layered architecture.
 * <p>
 * Diqqət et: burada qatları ƏL İLƏ quraşdırırıq (manual dependency injection).
 * Controller -> Service -> Repository zənciri interface-lər üzərindən bağlanır.
 * Lesson 4-də Spring bu "wiring"-i avtomatik edəcək.
 */
public class Main {

    public static void main(String[] args) {
        // 1) Repository qatı (in-memory implementasiya)
        UserRepository userRepository = new InMemoryUserRepository();
        TaskRepository taskRepository = new SortedInMemoryTaskRepository();

        // 2) Service qatı (business logic) - yalnız interface-dən asılıdır
        UserService userService = new UserServiceImpl(userRepository);
        NotificationService notificationService = new NoOpNotificationService();
        TaskService taskService = new TaskServiceImpl(taskRepository, userRepository, notificationService);

        // 3) Controller qatı (boundary)
        UserController userController = new UserController(userService, taskService);
        TaskController taskController = new TaskController(taskService);

        System.out.println("=== Task Management API - Lesson 2 (layered) ===\n");

        UserResponse darya = userController.create(new CreateUserRequest("Darya", "darya@example.com"));
        UserResponse ali = userController.create(new CreateUserRequest("Ali", "ali@example.com"));
        System.out.println("User-lər: " + userController.getAll());

        taskController.create(new CreateTaskRequest("Layered refactor", "controller/service/repo", Priority.HIGH, darya.id()));
        var t2 = taskController.create(new CreateTaskRequest("DTO mapping öyrən", null, Priority.MEDIUM, darya.id()));
        taskController.create(new CreateTaskRequest("SOLID təkrar", null, Priority.LOW, ali.id()));
        System.out.println("\nDarya-nın task-ları: " + userController.getUserTasks(darya.id()));

        // PATCH nümunəsi (yalnız status dəyişir)
        taskController.update(t2.id(), new UpdateTaskRequest(null, null, TaskStatus.DONE, null));
        System.out.println("\nYenilənmiş task: " + taskController.getById(t2.id()));

        List<TaskResponse> all = taskController.getAll();
        System.out.println("\nButun task-lar: " + all);

        // Xəta ssenarisi
        System.out.println("\nXəta ssenarisi:");
        try {
            userController.create(new CreateUserRequest("Dublikat", "darya@example.com"));
        } catch (DuplicateResourceException e) {
            System.out.println("  " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }

        System.out.println("\n=== Demo bitdi ===");
    }
}
