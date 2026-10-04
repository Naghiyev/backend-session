package az.training.taskmanagement.controller;

import az.training.taskmanagement.dto.category.response.CategoryResponse;
import az.training.taskmanagement.dto.category.request.CreateCategoryRequest;
import az.training.taskmanagement.dto.task.response.TaskResponse;
import az.training.taskmanagement.service.CategoryService;
import az.training.taskmanagement.service.TaskService;

import java.util.List;

public class CategoryController {

    private final CategoryService categoryService;
    private final TaskService taskService;

    public CategoryController(CategoryService categoryService, TaskService taskService) {
        this.categoryService = categoryService;
        this.taskService = taskService;
    }

    public CategoryResponse create(CreateCategoryRequest request) {
        return categoryService.createCategory(request);
    }

    public List<CategoryResponse> getAll() {
        return categoryService.getAllCategories();
    }

    public CategoryResponse getById(Long id) {
        return categoryService.getCategoryById(id);
    }

    public List<TaskResponse> getCategoryTasks(Long id) {
        return taskService.getTasksByCategory(id);
    }

    public void delete(Long id) {
        categoryService.deleteCategory(id);
    }
}
