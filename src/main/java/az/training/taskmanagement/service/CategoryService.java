package az.training.taskmanagement.service;

import az.training.taskmanagement.dto.CategoryResponse;
import az.training.taskmanagement.dto.CreateCategoryRequest;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CreateCategoryRequest request);

    CategoryResponse getCategoryById(Long id);

    void deleteCategory(Long id);

    List<CategoryResponse> getCategories();

}