package az.training.taskmanagement.controller;
import az.training.taskmanagement.dto.CategoryResponse;
import az.training.taskmanagement.dto.CreateCategoryRequest;
import az.training.taskmanagement.dto.TaskResponse;
import az.training.taskmanagement.dto.UpdateCategoryRequest;
import az.training.taskmanagement.service.CategoryService;
import az.training.taskmanagement.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final TaskService taskService;

    public CategoryController(CategoryService categoryService, TaskService taskService) {
        this.categoryService = categoryService;
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@RequestBody CreateCategoryRequest request) {
        CategoryResponse created = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<CategoryResponse> getAll() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public CategoryResponse getById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @GetMapping("/{id}/tasks")
    public List<TaskResponse> getTasks(@PathVariable Long id) {
        return taskService.getTasksByCategory(id);
    }

    @PutMapping("/{id}")
    public CategoryResponse update(@PathVariable Long id, @RequestBody UpdateCategoryRequest request) {
        return categoryService.updateCategory(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
