package az.training.taskmanagement.service;

import az.training.taskmanagement.exceptions.DuplicateResourceException;
import az.training.taskmanagement.exceptions.InvalidInputException;
import az.training.taskmanagement.exceptions.ResourceNotFoundException;
import az.training.taskmanagement.model.Category;
import az.training.taskmanagement.repository.CategoryRepository;

import java.util.List;

/**
 * Category üçün business logic.
 */
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidInputException("name bos ola bilmez");
        }
        if (categoryRepository.existsByName(name)) {
            throw new DuplicateResourceException("Bu kateqoriya artıq mövcuddur: " + name);
        }
        return categoryRepository.save(new Category(null, name));
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public void deleteCategory(Long id) {
        getCategoryById(id); // mövcudluğu yoxla
        categoryRepository.deleteById(id);
    }
}
