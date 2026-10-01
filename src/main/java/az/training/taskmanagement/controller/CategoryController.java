package az.training.taskmanagement.controller;

import az.training.taskmanagement.dto.CategoryResponse;
import az.training.taskmanagement.dto.CreateCategoryRequest;
import az.training.taskmanagement.service.CategoryService;

import java.util.List;

public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public CategoryResponse create(CreateCategoryRequest request) {
        return categoryService.createCategory(request);
    }

    public CategoryResponse getById(Long id) {
        return categoryService.getCategoryById(id);
    }

    public List<CategoryResponse> getAll() {
        return categoryService.getAllCategories();
    }

    public CategoryResponse deactivate(Long id) {
        return categoryService.deactivateCategory(id);
    }

    public void delete(Long id) {
        categoryService.deleteCategory(id);
    }
}
