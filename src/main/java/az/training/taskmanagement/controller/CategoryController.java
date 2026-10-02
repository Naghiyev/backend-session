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

    // POST /categories
    public CategoryResponse create(CreateCategoryRequest request) {
        return categoryService.createCategory(request);
    }

    // GET /categories/{id}
    public CategoryResponse getById(Long id) {
        return categoryService.getCategoryById(id);
    }

    // GET /categories
    public List<CategoryResponse> getAll() {
        return categoryService.getAllCategories();
    }

    // PATCH /categories/{id}
    public CategoryResponse update(Long id, UpdateCategoryRequest request) {
        return categoryService.updateCategory(id, request);
    }

    // DELETE /categories/{id}
    public void delete(Long id) {
        categoryService.deleteCategory(id);
    }
}
