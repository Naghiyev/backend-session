package az.training.taskmanagement.service.impl;

import az.training.taskmanagement.dto.CategoryResponse;
import az.training.taskmanagement.dto.CreateCategoryRequest;
import az.training.taskmanagement.dto.UpdateCategoryRequest;
import az.training.taskmanagement.exception.ResourceNotFoundException;
import az.training.taskmanagement.exception.ValidationException;
import az.training.taskmanagement.mapper.CategoryMapper;
import az.training.taskmanagement.model.Category;
import az.training.taskmanagement.repository.CategoryRepository;
import az.training.taskmanagement.service.CategoryService;
import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;
    public CategoryServiceImpl(CategoryRepository repository) { this.repository = repository; }

    public CategoryResponse createCategory(CreateCategoryRequest request) {
        validateName(request == null ? null : request.name());
        return CategoryMapper.toResponse(repository.save(CategoryMapper.toEntity(request)));
    }
    public CategoryResponse getCategoryById(Long id) {
        return CategoryMapper.toResponse(findOrThrow(id));
    }
    public List<CategoryResponse> getAllCategories() {
        return repository.findAll().stream().map(CategoryMapper::toResponse).toList();
    }
    public synchronized CategoryResponse updateCategory(Long id, UpdateCategoryRequest request) {
        validateName(request == null ? null : request.name());
        Category category = findOrThrow(id);
        category.setName(request.name());
        return CategoryMapper.toResponse(repository.save(category));
    }
    public synchronized void deleteCategory(Long id) {
        findOrThrow(id);
        repository.deleteById(id);
    }
    private Category findOrThrow(Long id) {
        return repository.findById(id).orElseThrow(() -> ResourceNotFoundException.of("Category", id));
    }
    private void validateName(String name) {
        if (name == null || name.isBlank()) throw new ValidationException("Category adı boş ola bilməz");
    }
}
