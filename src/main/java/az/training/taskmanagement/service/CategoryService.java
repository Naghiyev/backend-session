package az.training.taskmanagement.service;

import az.training.taskmanagement.dto.CategoryResponse;
import az.training.taskmanagement.dto.CreateCategoryRequest;
import az.training.taskmanagement.dto.UpdateCategoryRequest;
import az.training.taskmanagement.exception.ResourceNotFoundException;
import az.training.taskmanagement.exception.ValidationException;
import az.training.taskmanagement.mapper.CategoryMapper;
import az.training.taskmanagement.model.Category;
import az.training.taskmanagement.repository.CategoryRepository;

import java.time.LocalDateTime;
import java.util.List;

public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse createCategory(CreateCategoryRequest request) {

        if (request.name() == null || request.name().isBlank()) {
            throw new ValidationException("category name boş ola bilməz");
        }

        Category saved =
                categoryRepository.save(CategoryMapper.toEntity(request));

        return CategoryMapper.toResponse(saved);
    }

    public CategoryResponse getCategoryById(Long id) {
        return CategoryMapper.toResponse(findCategoryOrThrow(id));
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryMapper::toResponse)
                .toList();
    }

    public CategoryResponse updateCategory(
            Long id,
            UpdateCategoryRequest request
    ) {

        Category category = findCategoryOrThrow(id);

        if (request.name() != null) {
            if (request.name().isBlank()) {
                throw new ValidationException(
                        "category name boş ola bilməz"
                );
            }

            category.setName(request.name());
        }

        if (request.description() != null) {
            category.setDescription(request.description());
        }

        category.setUpdatedAt(LocalDateTime.now());

        return CategoryMapper.toResponse(
                categoryRepository.save(category)
        );
    }

    public void deleteCategory(Long id) {
        findCategoryOrThrow(id);
        categoryRepository.deleteById(id);
    }

    private Category findCategoryOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(
                        () -> ResourceNotFoundException.of(
                                "Category",
                                id
                        )
                );
    }
}