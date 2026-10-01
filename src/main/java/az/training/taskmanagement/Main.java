package az.training.taskmanagement;

import az.training.taskmanagement.model.Priority;
import az.training.taskmanagement.model.Task;
import az.training.taskmanagement.model.TaskStatus;
import az.training.taskmanagement.model.User;
import az.training.taskmanagement.repository.CategoryRepository;
import az.training.taskmanagement.repository.TaskRepository;
import az.training.taskmanagement.repository.UserRepository;
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
        UserService userService = new UserService(userRepository);
        TaskService taskService = new TaskService(taskRepository, userRepository,categoryRepository);

        System.out.println("=== Task Management API - Lesson 1 (in-memory) ===\n");

        // CREATE user
        User darya = userService.createUser("Darya", "darya@example.com");
        User ali = userService.createUser("Ali", "ali@example.com");
        System.out.println("Yaradılan user-lər:");
        userService.getAllUsers().forEach(u -> System.out.println("  " + u));

        // CREATE tasks
        Task t1 = taskService.createTask("Backend syllabus hazırla",
                "8 dərslik plan", Priority.HIGH, darya.getId(),3L);
        Task t2 = taskService.createTask("Repository nümunəsi yaz",
                "In-memory CRUD", Priority.MEDIUM, darya.getId(),1L);
        Task t3 = taskService.createTask("Java essentials təkrar et",
                null, Priority.LOW, ali.getId(),2L);
        System.out.println("\nYaradılan task-lar:");
        taskService.getAllTasks().forEach(t -> System.out.println("  " + t));

        // UPDATE status
        taskService.updateStatus(t1.getId(), TaskStatus.IN_PROGRESS);
        System.out.println("\nStatus dəyişdi -> " + taskService.getTaskById(t1.getId()));

        // FIND by user
        System.out.println("\nDarya-nın task-ları:");
        taskService.getTasksByUser(darya.getId()).forEach(t -> System.out.println("  " + t));

        // DELETE
        taskService.deleteTask(t3.getId());
        System.out.println("\nt3 silindikdən sonra ümumi task sayı: "
                + taskService.getAllTasks().size());

        // Xəta ssenarisi (validation)
        System.out.println("\nXəta ssenarisi:");
        try {
            userService.createUser("Dublikat", "darya@example.com");
        } catch (IllegalArgumentException e) {
            System.out.println("  Gözlənilən xəta: " + e.getMessage());
        }

        System.out.println("\n=== Demo bitdi ===");
    }
}
