package az.training.taskmanagement;

import az.training.taskmanagement.controller.CategoryController;
import az.training.taskmanagement.controller.TaskController;
import az.training.taskmanagement.controller.UserController;
import az.training.taskmanagement.dto.category.request.CreateCategoryRequest;
import az.training.taskmanagement.dto.category.response.CategoryResponse;
import az.training.taskmanagement.dto.task.request.CreateTaskRequest;
import az.training.taskmanagement.dto.task.request.UpdateTaskRequest;
import az.training.taskmanagement.dto.user.request.CreateUserRequest;
import az.training.taskmanagement.dto.user.request.UpdateUserRequest;
import az.training.taskmanagement.dto.user.response.UserResponse;
import az.training.taskmanagement.exception.DuplicateResourceException;
import az.training.taskmanagement.exception.ValidationException;
import az.training.taskmanagement.model.Priority;
import az.training.taskmanagement.model.TaskStatus;
import az.training.taskmanagement.repository.CategoryRepository;
import az.training.taskmanagement.repository.TaskRepository;
import az.training.taskmanagement.repository.UserRepository;
import az.training.taskmanagement.service.CategoryService;
import az.training.taskmanagement.service.TaskService;
import az.training.taskmanagement.service.UserService;

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
        UserRepository userRepository = new UserRepository();
        TaskRepository taskRepository = new TaskRepository();
        CategoryRepository categoryRepository = new CategoryRepository();

        // 2) Service qatı (business logic) - yalnız interface-dən asılıdır
        UserService userService = new UserService(userRepository);
        TaskService taskService = new TaskService(taskRepository, userRepository, categoryRepository);
        CategoryService categoryService = new CategoryService(categoryRepository, taskRepository);

        // 3) Controller qatı (boundary)
        UserController userController = new UserController(userService, taskService);
        TaskController taskController = new TaskController(taskService);
        CategoryController categoryController = new CategoryController(categoryService, taskService);

        System.out.println("=== Task Management API - Lesson 2 (layered) ===\n");

        UserResponse darya = userController.create(new CreateUserRequest("Darya", "darya@example.com"));
        UserResponse ali = userController.create(new CreateUserRequest("Ali", "ali@example.com"));
        System.out.println("User-lər: " + userController.getAll());

        CategoryResponse study = categoryController.create(new CreateCategoryRequest("Study", "Öyrənmə task-ları"));
        CategoryResponse work = categoryController.create(new CreateCategoryRequest("Work", null));
        System.out.println("Kateqoriyalar: " + categoryController.getAll());

        taskController.create(new CreateTaskRequest("Layered refactor", "controller/service/repo", Priority.HIGH, darya.id(), work.id()));
        var t2 = taskController.create(new CreateTaskRequest("DTO mapping öyrən", null, Priority.MEDIUM, darya.id(), study.id()));
        taskController.create(new CreateTaskRequest("SOLID təkrar", null, Priority.LOW, ali.id(), study.id()));

        System.out.println("\nStudy kateqoriyasının task-ları: " + categoryController.getCategoryTasks(study.id()));

        // PATCH nümunəsi (yalnız status dəyişir)
        taskController.update(t2.id(), new UpdateTaskRequest(null, null, TaskStatus.DONE, null));
        System.out.println("\nYenilənmiş task: " + taskController.getById(t2.id()));

        System.out.println("\nUser update: " + userController.update(ali.id(), new UpdateUserRequest("Ali Əliyev", null)));

        System.out.println("\nXəta ssenarisi:");
        try {
            userController.create(new CreateUserRequest("Dublikat", "darya@example.com"));
        } catch (DuplicateResourceException e) {
            System.out.println("  " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }

        try {
            userController.update(ali.id(), new UpdateUserRequest(null, "darya@example.com"));
        } catch (DuplicateResourceException e) {
            System.out.println("  " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }

        try {
            categoryController.create(new CreateCategoryRequest("study", null));
        } catch (DuplicateResourceException e) {
            System.out.println("  " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
        try {
            categoryController.delete(study.id());
        } catch (ValidationException e) {
            System.out.println("  " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }

        System.out.println("\n=== Demo bitdi ===");
    }
}
