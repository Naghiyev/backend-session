package az.training.taskmanagement;

import az.training.taskmanagement.controller.CategoryController;
import az.training.taskmanagement.controller.TaskController;
import az.training.taskmanagement.controller.UserController;
import az.training.taskmanagement.dto.*;
import az.training.taskmanagement.exception.DuplicateResourceException;
import az.training.taskmanagement.exception.ResourceNotFoundException;
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
 *
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
        TaskService taskService = new TaskService(taskRepository, userRepository);
        CategoryService categoryService = new CategoryService(categoryRepository);

        // 3) Controller qatı (boundary)
        UserController userController = new UserController(userService, taskService);
        TaskController taskController = new TaskController(taskService);
        CategoryController categoryController = new CategoryController(categoryService);

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

        // Xəta ssenarisi
        System.out.println("\nXəta ssenarisi:");
        try {
            userController.create(new CreateUserRequest("Dublikat", "darya@example.com"));
        } catch (DuplicateResourceException e) {
            System.out.println("  " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }


        // ===== Homework 2: Category =====

        System.out.println("\n--- Category demo ---");
        CategoryResponse work = categoryController.create(
                new CreateCategoryRequest("İş", "İşlə bağlı tapşırıqlar", "#FF5733"));
        CategoryResponse study = categoryController.create(
                new CreateCategoryRequest("Təhsil", "Öyrənmə tapşırıqları", "#3498DB"));
        System.out.println("Kateqoriyalar: " + categoryController.getAll());

        System.out.println("Passiv edildi: " + categoryController.deactivate(study.id()));

        categoryController.delete(work.id());
        System.out.println("Silindikdən sonra: " + categoryController.getAll());

        System.out.println("\nCategory xəta ssenariləri:");
        try {
            categoryController.create(new CreateCategoryRequest("Təhsil", null, null));
        } catch (DuplicateResourceException e) {
            System.out.println("  " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
        try {
            categoryController.getById(99L);
        } catch (ResourceNotFoundException e) {
            System.out.println("  " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }


        // ===== Homework 2: updateUser =====
        System.out.println("\n--- User update demo ---");
        System.out.println("Yalnız ad: " + userController.update(ali.id(), new UpdateUserRequest("Əli", null)));
        System.out.println("Yalnız email: " + userController.update(ali.id(), new UpdateUserRequest(null, "ali.new@example.com")));
        System.out.println("Eyni email (xəta yox): " + userController.update(ali.id(), new UpdateUserRequest(null, "ali.new@example.com")));
        try {
            userController.update(ali.id(), new UpdateUserRequest(null, "darya@example.com"));
        } catch (DuplicateResourceException e) {
            System.out.println("  " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
        try {
            userController.update(99L, new UpdateUserRequest("Kimsə", null));
        } catch (ResourceNotFoundException e) {
            System.out.println("  " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }

        System.out.println("\n=== Demo bitdi ===");
    }
}
