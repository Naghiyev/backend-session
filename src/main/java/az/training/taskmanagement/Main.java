package az.training.taskmanagement;

import az.training.taskmanagement.exception.*;
import az.training.taskmanagement.model.*;
import az.training.taskmanagement.repository.CategoryRepository;
import az.training.taskmanagement.repository.TaskRepository;
import az.training.taskmanagement.repository.UserRepository;
import az.training.taskmanagement.service.CategoryService;
import az.training.taskmanagement.service.TaskService;
import az.training.taskmanagement.service.UserService;

/**
 * Lesson 1 demo.
 * <p>
 * Bu class hələ REST API deyil - sadəcə backend model-in,
 * repository və service qatlarının necə işlədiyini console-da göstərir.
 * <p>
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
        TaskService taskService = new TaskService(taskRepository, userRepository, categoryRepository);
        CategoryService categoryService = new CategoryService(categoryRepository);

        System.out.println("=== Task Management API - Lesson 1 (in-memory) ===\n");

        // CREATE user
        User darya = userService.createUser("Darya", "darya@example.com");
        User ali = userService.createUser("Ali", "ali@example.com");
        System.out.println("\nYaradılan user-lər:");
        userService.getAllUsers().forEach(u -> System.out.println("  " + u));


        //CREATE category
        Category work = categoryService.createCategory("Work");
        Category study = categoryService.createCategory("Study");

        System.out.println("\nYaradilan category-lər");
        categoryService.getAllCategories().forEach(c -> System.out.println(" " + c));


        // CREATE tasks
        Task t1 = taskService.createTask("Backend syllabus hazırla",
                "8 dərslik plan", Priority.HIGH, darya.getId(), study.id());
        Task t2 = taskService.createTask("Repository nümunəsi yaz",
                "In-memory CRUD", Priority.MEDIUM, darya.getId(), work.id());
        Task t3 = taskService.createTask("Java essentials təkrar et",
                null, Priority.LOW, ali.getId(), study.id());
        System.out.println("\nYaradılan task-lar:");
        taskService.getAllTasks().forEach(t -> System.out.println("  " + t));


        // UPDATE status
        taskService.updateStatus(t1.getId(), TaskStatus.IN_PROGRESS);
        System.out.println("\nStatus dəyişdi -> " + taskService.getTaskById(t1.getId()));


        //FIND BY USER
        System.out.println("\nDarya-nın task-ları:");
        taskService.getTasksByUser(darya.getId()).forEach(t -> System.out.println(" " + t));


        //FIND BY STATUS
        System.out.println("\nIN_PROGRESS task-lar:");
        taskService.getTasksByStatus(TaskStatus.IN_PROGRESS).forEach(t -> System.out.println(" " + t));


        // DELETE
        taskService.deleteTask(t3.getId());
        System.out.println("\nt3 silindikdən sonra ümumi task sayı: "
                + taskService.getAllTasks().size());


        // Xəta ssenarisi (validation)
        System.out.println("\nXəta ssenarisi:");

        //dublicate email
        try {
            userService.createUser("Dublikat", "darya@example.com");
        } catch (EmailAlreadyExistsException e) {
            System.out.println(" Dublicate email: " + e.getMessage());
        }


        //empty task title
        try {
            taskService.createTask(
                    "",
                    "Test description",
                    Priority.MEDIUM,
                    darya.getId(),
                    study.id()
            );
        } catch (InvalidTaskException e) {
            System.out.println(
                    "  Invalid task: "
                            + e.getMessage()
            );
        }


        // empty category name
        try {
            categoryService.createCategory("");
        } catch (InvalidCategoryException e) {
            System.out.println(
                    "  Invalid category: "
                            + e.getMessage()
            );
        }


        // non-exist user
        try {
            taskService.createTask(
                    "Test task",
                    "Test",
                    Priority.MEDIUM,
                    999L,
                    study.id()
            );
        } catch (UserNotFoundException e) {
            System.out.println(
                    " User not found: " +
                            e.getMessage()
            );
        }


        // Non-existing category
        try {

            taskService.createTask(
                    "Test task",
                    "Test",
                    Priority.MEDIUM,
                    darya.getId(),
                    999L
            );

        } catch (CategoryNotFoundException e) {

            System.out.println(
                    "  Category not found: "
                            + e.getMessage()
            );
        }


        // Non-existing task
        try {

            taskService.getTaskById(999L);

        } catch (TaskNotFoundException e) {

            System.out.println(
                    "  Task not found: "
                            + e.getMessage()
            );
        }


        System.out.println("\n=== Demo bitdi ===");
    }
}
