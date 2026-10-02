package az.training.taskmanagement;

import az.training.taskmanagement.exception.DuplicateEmailException;
import az.training.taskmanagement.exception.ResourceInUseException;
import az.training.taskmanagement.exception.TaskManagementException;
import az.training.taskmanagement.model.*;
import az.training.taskmanagement.repository.CategoryRepository;
import az.training.taskmanagement.repository.TaskRepository;
import az.training.taskmanagement.repository.UserRepository;
import az.training.taskmanagement.service.CategoryService;
import az.training.taskmanagement.service.TaskService;
import az.training.taskmanagement.service.UserService;

/**
 * Lesson 1 demo.
 *
 * Bu class hələ REST API deyil - sadəcə backend model-in,
 * repository və service qatlarının necə işlədiyini console-da göstərir.
 *
 * İşə salmaq:  mvn -q compile exec:java
 */
public class Main {

    public static void main(String[] args) {
        // Qatları əl ilə "quraşdırırıq" (manual wiring).
        // Lesson 4-də bunu Spring avtomatik edəcək (Dependency Injection).
        UserRepository userRepository = new UserRepository();
        TaskRepository taskRepository = new TaskRepository();
        CategoryRepository categoryRepository = new CategoryRepository();

        UserService userService = new UserService(userRepository, taskRepository);
        CategoryService categoryService = new CategoryService(categoryRepository, taskRepository);
        TaskService taskService = new TaskService(taskRepository, userRepository, categoryRepository);

        System.out.println("=== Task Management API - Lesson 1 (in-memory) ===\n");

        // CREATE user
        User darya = userService.createUser("Darya", "darya@example.com");
        User ali = userService.createUser("Ali", "ali@example.com");
        System.out.println("Yaradılan user-lər:");
        userService.getAllUsers().forEach(u -> System.out.println("  " + u));

        // CREATE category
        Category work = categoryService.createCategory("Work", "İş tapşırıqları");
        Category study = categoryService.createCategory("Study", "Öyrənmə");
        System.out.println("\nYaradılan category-lər:");
        categoryService.getAllCategories().forEach(c -> System.out.println("  " + c));

        // CREATE tasks (service daxilində Task.builder() istifadə olunur)
        Task t1 = taskService.createTask("Backend syllabus hazırla",
                "8 dərslik plan", Priority.HIGH, darya.getId(), work.id());
        Task t2 = taskService.createTask("Repository nümunəsi yaz",
                "In-memory CRUD", Priority.MEDIUM, darya.getId(), work.id());
        Task t3 = taskService.createTask("Java essentials təkrar et",
                null, Priority.LOW, ali.getId(), study.id());
        Task t4 = taskService.createTask("Alış-veriş",
                null, null, ali.getId());   // category-siz, priority default MEDIUM
        System.out.println("\nYaradılan task-lar:");
        taskService.getAllTasks().forEach(t -> System.out.println("  " + t));

        // UPDATE status
        taskService.updateStatus(t1.getId(), TaskStatus.IN_PROGRESS);
        taskService.updateStatus(t2.getId(), TaskStatus.DONE);
        System.out.println("\nStatus dəyişdi -> " + taskService.getTaskById(t1.getId()));

        // FIND by status (Ev tapşırığı 3)
        System.out.println("\nStatus-a görə task-lar:");
        for (TaskStatus status : TaskStatus.values()) {
            System.out.println("  " + status + ": " + taskService.getTasksByStatus(status));
        }

        // FIND by user
        System.out.println("\nDarya-nın task-ları:");
        taskService.getTasksByUser(darya.getId()).forEach(t -> System.out.println("  " + t));

        // FIND by category + assign category
        taskService.assignCategory(t4.getId(), study.id());
        System.out.println("\nStudy category-sinin task-ları:");
        taskService.getTasksByCategory(study.id()).forEach(t -> System.out.println("  " + t));

        // DELETE
        taskService.deleteTask(t3.getId());
        System.out.println("\nt3 silindikdən sonra ümumi task sayı: "
                + taskService.getAllTasks().size());

        // Xəta ssenariləri (specific exception-lar)
        System.out.println("\nXəta ssenariləri:");

        // 1) Konkret exception-u tutmaq
        try {
            userService.createUser("Dublikat", "darya@example.com");
        } catch (DuplicateEmailException e) {
            System.out.println("  Gözlənilən xəta: " + e.getMessage());
        }

        try {
            userService.deleteUser(darya.getId());
        } catch (ResourceInUseException e) {
            System.out.println("  Gözlənilən xəta: " + e.getMessage());
        }

        // 2) Baza sinif ilə bütün business xətalarını tutmaq
        runExpectingError(() -> taskService.createTask("Test", null, null, 99L));
        runExpectingError(() -> taskService.createTask("Test", null, null, ali.getId(), 42L));
        runExpectingError(() -> taskService.createTask("   ", null, null, ali.getId()));
        runExpectingError(() -> taskService.getTaskById(500L));
        runExpectingError(() -> userService.createUser("Vəli", "yanlis-email"));
        runExpectingError(() -> categoryService.createCategory("work", null));
        runExpectingError(() -> categoryService.deleteCategory(work.id()));

        System.out.println("\n=== Demo bitdi ===");
    }

    /** Xəta gözlənilən əməliyyatı işlədir və exception-un tipini + mesajını çap edir. */
    private static void runExpectingError(Runnable action) {
        try {
            action.run();
            System.out.println("  XƏTA GÖZLƏNİLİRDİ, amma əməliyyat uğurlu oldu!");
        } catch (TaskManagementException e) {
            System.out.println("  " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }
}
