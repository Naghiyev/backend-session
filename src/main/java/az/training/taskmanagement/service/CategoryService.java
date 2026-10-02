package az.training.taskmanagement.service;

import az.training.taskmanagement.model.Category;
import az.training.taskmanagement.repository.CategoryRepository;

import java.util.List;

public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(String name, String description) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name boş ola bilməz");
        }
        Category category = new Category(null, name, description);
        return categoryRepository.save(category);
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category tapılmadı: id=" + id));
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public void deleteTask(Long id) {
        getCategoryById(id);
        categoryRepository.deleteById(id);
    }
}
