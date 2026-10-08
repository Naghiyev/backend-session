package az.training.taskmanagement.controller;


import az.training.taskmanagement.dto.CategoryResponse;
import az.training.taskmanagement.dto.CreateCategoryRequest;
import az.training.taskmanagement.service.impl.CategoryService;


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


    // GET /categories
    public List<CategoryResponse> getAllCategories() {
        return categoryService.getCategories();
    }

    // GET /categories{id}
    public CategoryResponse getCategoryById(Long id) {
        return categoryService.getCategoryById(id);
    }

    // DELETE /categories{id}
    public void delete(Long id) {
        categoryService.deleteCategory(id);
    }
}
