package az.training.taskmanagement.service;

import az.training.taskmanagement.exception.AlreadyExistsException;
import az.training.taskmanagement.exception.CategoryNotFoundException;
import az.training.taskmanagement.exception.ValidationException;
import az.training.taskmanagement.model.Category;
import az.training.taskmanagement.repository.CategoryRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Category üçün business logic.
 */
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(String name, String description, String color) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("name boş ola bilməz");
        }
        if (categoryRepository.existsByName(name)) {
            throw new AlreadyExistsException("Bu kateqoriya artıq mövcuddur: " + name);
        }
        Category category = new Category(null, name, description, color);
        return categoryRepository.save(category);
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category deactivateCategory(Long id) {
        Category category = getCategoryById(id);
        category.setActive(false);
        category.setUpdatedAt(LocalDateTime.now());
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        getCategoryById(id); // mövcudluğu yoxla
        categoryRepository.deleteById(id);
    }
}