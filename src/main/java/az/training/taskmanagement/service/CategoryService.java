package az.training.taskmanagement.service;

import az.training.taskmanagement.dto.CategoryRequest;
import az.training.taskmanagement.dto.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest categoryRequest);

    CategoryResponse getCategoryById(Long id);

    List<CategoryResponse> getAllCategories();

    void deleteCategoryById(Long id);
}
