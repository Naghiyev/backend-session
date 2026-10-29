package az.training.taskmanagement.service.impl;

import az.training.taskmanagement.dto.CategoryRequest;
import az.training.taskmanagement.dto.CategoryResponse;
import az.training.taskmanagement.exception.ResourceNotFoundException;
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
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        if (categoryRequest.name() == null||categoryRequest.name().isBlank()) {
            throw new IllegalArgumentException("Category name is required");
        }
        if (categoryRequest.description() == null||categoryRequest.description().isBlank()) {
            throw new IllegalArgumentException("Category description is required");
        }
        Category saved=categoryRepository.saveCategory(CategoryMapper.toEntity(categoryRequest));
        return CategoryMapper.toResponse(saved);
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        return CategoryMapper.toResponse(findCategoryOrThrow(id));
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAllCategories().stream()
                .map(CategoryMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteCategoryById(Long id) {
        findCategoryOrThrow(id);
        categoryRepository.deleteCategoryById(id);

    }
    private Category findCategoryOrThrow(Long id) {
        return categoryRepository.findCategoryById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Category",id));
    }
}
