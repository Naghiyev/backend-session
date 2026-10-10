package az.training.taskmanagement.service;

import java.util.List;

import az.training.taskmanagement.exception.CategoryNotFoundException;
import az.training.taskmanagement.model.Category;
import az.training.taskmanagement.repository.inMemory.InMemoryCategoryRepository;

public class CategoryService {

    private final InMemoryCategoryRepository categoryRepository;

    public CategoryService(InMemoryCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Category name boş ola bilməz");
        }

        Category category = new Category(null, name);

        return categoryRepository.save(category);
    }

    public Category getCategoryById(Long id) {

        return categoryRepository.findById(id)
                .orElseThrow(() ->
                    new CategoryNotFoundException(
                        "Category tapılmadı: id=" + id
                    )
                );
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}