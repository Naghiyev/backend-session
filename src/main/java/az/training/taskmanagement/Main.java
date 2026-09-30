package az.training.taskmanagement;

import az.training.taskmanagement.exception.AlreadyExistsException;
import az.training.taskmanagement.exception.NotFoundException;
import az.training.taskmanagement.exception.ValidationException;
import az.training.taskmanagement.model.Category;
import az.training.taskmanagement.model.Priority;
import az.training.taskmanagement.model.Task;
import az.training.taskmanagement.model.TaskStatus;
import az.training.taskmanagement.model.User;
import az.training.taskmanagement.repository.CategoryRepository;
import az.training.taskmanagement.repository.TaskRepository;
import az.training.taskmanagement.repository.UserRepository;
import az.training.taskmanagement.service.CategoryService;
import az.training.taskmanagement.service.TaskService;
import az.training.taskmanagement.service.UserService;

/**
 * Lesson 1 demo + Homework 1.
 */
public class Main {

    public static void main(String[] args) {
        // Qatları əl ilə "quraşdırırıq" (manual wiring).
        UserRepository userRepository = new UserRepository();
        TaskRepository taskRepository = new TaskRepository();
        CategoryRepository categoryRepository = new CategoryRepository();

        UserService userService = new UserService(userRepository);
        CategoryService categoryService = new CategoryService(categoryRepository);
        TaskService taskService = new TaskService(taskRepository, userRepository, categoryRepository);

        System.out.println("=== Task Management API - Homework 1 (in-memory) ===\n");

        // CREATE user
        User darya = userService.createUser("Darya", "darya@example.com");
        User ali = userService.createUser("Ali", "ali@example.com");
        System.out.println("Yaradılan user-lər:");
        userService.getAllUsers().forEach(u -> System.out.println("  " + u));

        // CREATE category (Tapşırıq 1)
        Category work = categoryService.createCategory("İş", "İşlə bağlı tapşırıqlar", "#FF5733");
        Category study = categoryService.createCategory("Təhsil", "Öyrənmə tapşırıqları", "#3498DB");
        System.out.println("\nYaradılan kateqoriyalar:");
        categoryService.getAllCategories().forEach(c -> System.out.println("  " + c));

        // CREATE tasks - categoryId ilə (Tapşırıq 2)
        Task t1 = taskService.createTask("Backend syllabus hazırla",
                "8 dərslik plan", Priority.HIGH, darya.getId(), work.getId());
        Task t2 = taskService.createTask("Repository nümunəsi yaz",
                "In-memory CRUD", Priority.MEDIUM, darya.getId(), work.getId());
        Task t3 = taskService.createTask("Java essentials təkrar et",
                null, Priority.LOW, ali.getId(), study.getId());
        taskService.createTask("Kateqoriyasız tapşırıq",
                null, null, ali.getId(), null);
        System.out.println("\nYaradılan task-lar:");
        taskService.getAllTasks().forEach(t -> System.out.println("  " + t));

        // UPDATE status
        taskService.updateStatus(t1.getId(), TaskStatus.IN_PROGRESS);
        taskService.updateStatus(t2.getId(), TaskStatus.DONE);
        System.out.println("\nStatus dəyişdi -> " + taskService.getTaskById(t1.getId()));

        // FIND by user
        System.out.println("\nDarya-nın task-ları:");
        taskService.getTasksByUser(darya.getId()).forEach(t -> System.out.println("  " + t));

        // FIND by status (Tapşırıq 3)
        for (TaskStatus status : TaskStatus.values()) {
            System.out.println("\n" + status + " statuslu task-lar:");
            taskService.getTasksByStatus(status).forEach(t -> System.out.println("  " + t));
        }

        // DELETE
        taskService.deleteTask(t3.getId());
        System.out.println("\nt3 silindikdən sonra ümumi task sayı: "
                + taskService.getAllTasks().size());

        // Xəta ssenariləri - xüsusi exception-lar (Tapşırıq 4)
        System.out.println("\nXəta ssenariləri:");
        try {
            userService.createUser("Dublikat", "darya@example.com");
        } catch (AlreadyExistsException e) {
            System.out.println("  [AlreadyExists] " + e.getMessage());
        }
        try {
            taskService.getTaskById(t3.getId());
        } catch (NotFoundException e) {
            System.out.println("  [NotFound] " + e.getMessage());
        }
        try {
            taskService.createTask("Olmayan user", null, null, 99L, null);
        } catch (NotFoundException e) {
            System.out.println("  [NotFound] " + e.getMessage());
        }
        try {
            taskService.createTask("Olmayan kateqoriya", null, null, ali.getId(), 99L);
        } catch (NotFoundException e) {
            System.out.println("  [NotFound] " + e.getMessage());
        }
        try {
            categoryService.deactivateCategory(study.getId());
            taskService.createTask("Passiv kateqoriya", null, null, ali.getId(), study.getId());
        } catch (ValidationException e) {
            System.out.println("  [Validation] " + e.getMessage());
        }
        try {
            categoryService.createCategory("Şəxsi", null, "qırmızı");
        } catch (ValidationException e) {
            System.out.println("  [Validation] " + e.getMessage());
        }

        System.out.println("\n=== Demo bitdi ===");
    }
}