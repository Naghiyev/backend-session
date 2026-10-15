package az.training.taskmanagement.service.impl;

import az.training.taskmanagement.dto.CategoryResponse;
import az.training.taskmanagement.dto.CreateCategoryRequest;
import az.training.taskmanagement.dto.UpdateCategoryRequest;
import az.training.taskmanagement.exception.DuplicateResourceException;
import az.training.taskmanagement.exception.ResourceNotFoundException;
import az.training.taskmanagement.exception.ValidationException;
import az.training.taskmanagement.mapper.CategoryMapper;
import az.training.taskmanagement.model.Category;
import az.training.taskmanagement.repository.CategoryRepository;
import az.training.taskmanagement.service.CategoryService;

import java.util.List;

@org.springframework.stereotype.Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        validateName(request.name());
        if (categoryRepository.existsByName(request.name())) {
            throw new DuplicateResourceException("Bu kateqoriya artıq mövcuddur: " + request.name());
        }
        Category saved = categoryRepository.save(CategoryMapper.toEntity(request));
        return CategoryMapper.toResponse(saved);
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        return CategoryMapper.toResponse(findCategoryOrThrow(id));
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryMapper::toResponse)
                .toList();
    }

    @Override
    public CategoryResponse updateCategory(Long id, UpdateCategoryRequest request) {
        Category category = findCategoryOrThrow(id);
        validateName(request.name());

        boolean nameChanged = !category.getName().equalsIgnoreCase(request.name());
        if (nameChanged && categoryRepository.existsByName(request.name())) {
            throw new DuplicateResourceException("Bu kateqoriya artıq mövcuddur: " + request.name());
        }

        category.setName(request.name());
        category.setDescription(request.description());
        return CategoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long id) {
        findCategoryOrThrow(id);
        categoryRepository.deleteById(id);
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("name boş ola bilməz");
        }
    }

    private Category findCategoryOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Category", id));
    }
}