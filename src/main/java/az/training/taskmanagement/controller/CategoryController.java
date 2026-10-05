package az.training.taskmanagement.controller;

import az.training.taskmanagement.dto.CategoryResponse;
import az.training.taskmanagement.dto.CreateCategoryRequest;
import az.training.taskmanagement.dto.UpdateCategoryRequest;
import az.training.taskmanagement.service.CategoryService;

import java.util.List;

public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // POST /tasks
    public CategoryResponse create(CreateCategoryRequest request) {
        return categoryService.createCategory(request);
    }

    // GET /tasks
    public List<CategoryResponse> getAll() {
        return categoryService.getAllCategories();
    }

    // GET /tasks/{id}
    public CategoryResponse getById(Long id) {
        return categoryService.getCategoryById(id);
    }

    // PATCH /tasks/{id}
    public CategoryResponse update(Long id, UpdateCategoryRequest request) {
        return categoryService.updateCategory(id, request);
    }

    // DELETE /tasks/{id}
    public void delete(Long id) {
        categoryService.deleteCategory(id);
    }
}
