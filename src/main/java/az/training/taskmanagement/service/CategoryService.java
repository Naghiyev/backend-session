package az.training.taskmanagement.service;

import az.training.taskmanagement.model.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(Category category);
    List<Category> getAllCategories();
    Category getCategoryById(Long id);
}