package az.training.taskmanagement.service;

import az.training.taskmanagement.exception.CategoryNotFoundException;
import az.training.taskmanagement.exception.InvalidCategoryException;
import az.training.taskmanagement.model.Category;
import az.training.taskmanagement.repository.CategoryRepository;

import java.util.List;

public class CategoryService {
    public CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidCategoryException("Name bos ola bilmez");
        }
        Category category = new Category(null, name);
        return categoryRepository.save(category);
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category tapilmadi: id=" + id));
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public void deleteCategory(Long id) {
        getCategoryById(id);
        categoryRepository.deleteById(id);
    }
}
