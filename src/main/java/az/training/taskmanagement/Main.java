package az.training.taskmanagement;

import az.training.taskmanagement.exception.CategoryNotFoundException;
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
 * Lesson 1 demo (console).
 */
public class Main {

    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();
        TaskRepository taskRepository = new TaskRepository();
        CategoryRepository categoryRepository = new CategoryRepository();
        UserService userService = new UserService(userRepository);
        CategoryService categoryService = new CategoryService(categoryRepository);
        TaskService taskService = new TaskService(taskRepository, userRepository, categoryRepository);

        System.out.println("=== Task Management API - Lesson 1 (in-memory) ===\n");

        // CREATE users
        User darya = userService.createUser("Darya", "darya@example.com");
        User ali = userService.createUser("Ali", "ali@example.com");
        System.out.println("Users:");
        userService.getAllUsers().forEach(u -> System.out.println("  " + u));

        // CREATE categories
        Category c1 = categoryService.createCategory("Work", "Finish by 6pm");
        Category c2 = categoryService.createCategory("Personal", "Weekends");
        System.out.println("\nCategories:");
        categoryService.getAllCategories().forEach(c -> System.out.println("  " + c));

        // CREATE tasks
        Task t1 = taskService.createTask("Backend syllabus", "8 lesson plan",
                Priority.HIGH, darya.getId(), c1.id());
        Task t2 = taskService.createTask("Repository example", "In-memory CRUD",
                Priority.MEDIUM, darya.getId(), c2.id());
        Task t3 = taskService.createTask("Java essentials review", null,
                Priority.LOW, ali.getId(), c1.id());
        System.out.println("\nTasks:");
        taskService.getAllTasks().forEach(t -> System.out.println("  " + t));

        // UPDATE status
        taskService.updateStatus(t1.getId(), TaskStatus.IN_PROGRESS);
        System.out.println("\nStatus changed -> " + taskService.getTaskById(t1.getId()));

        // FIND by status
        System.out.println("\nTasks with status TODO:");
        taskService.getTasksByStatus(TaskStatus.TODO).forEach(t -> System.out.println("  " + t));

        System.out.println("\nTasks with status IN_PROGRESS:");
        taskService.getTasksByStatus(TaskStatus.IN_PROGRESS).forEach(t -> System.out.println("  " + t));

        // FIND by user
        System.out.println("\nDarya's tasks:");
        taskService.getTasksByUser(darya.getId()).forEach(t -> System.out.println("  " + t));

        // DELETE
        taskService.deleteTask(t3.getId());
        System.out.println("\nTask count after deleting t3: " + taskService.getAllTasks().size());

        // Error scenarios
        System.out.println("\nError scenarios:");
        try {
            userService.createUser("Duplicate", "darya@example.com");
        } catch (IllegalArgumentException e) {
            System.out.println("  Expected error: " + e.getMessage());
        }
        try {
            taskService.createTask("Test", null, Priority.LOW, darya.getId(), 999L);
        } catch (CategoryNotFoundException e) {
            System.out.println("  Expected error: " + e.getMessage());
        }

        System.out.println("\n=== Demo finished ===");
    }
}
