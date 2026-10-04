package az.training.taskmanagement.service;

import az.training.taskmanagement.dto.category.response.CategoryResponse;
import az.training.taskmanagement.dto.category.request.CreateCategoryRequest;
import az.training.taskmanagement.exception.DuplicateResourceException;
import az.training.taskmanagement.exception.ResourceNotFoundException;
import az.training.taskmanagement.exception.ValidationException;
import az.training.taskmanagement.mapper.CategoryMapper;
import az.training.taskmanagement.model.Category;
import az.training.taskmanagement.repository.CategoryRepository;
import az.training.taskmanagement.repository.TaskRepository;

import java.util.List;

public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final TaskRepository taskRepository;

    public CategoryService(CategoryRepository categoryRepository, TaskRepository taskRepository) {
        this.categoryRepository = categoryRepository;
        this.taskRepository = taskRepository;
    }

    public CategoryResponse createCategory(CreateCategoryRequest request) {

        if (request.name() == null || request.name().isBlank()) {
            throw new ValidationException("name boş ola bilməz");
        }
        if (categoryRepository.existsByName(request.name())) {
            throw new DuplicateResourceException("Bu kateqoriya artıq mövcuddur: " + request.name());
        }
        Category saved = categoryRepository.save(CategoryMapper.toEntity(request));
        return CategoryMapper.toResponse(saved);
    }

    public CategoryResponse getCategoryById(Long id) {
        return CategoryMapper.toResponse(findCategoryOrThrow(id));
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryMapper::toResponse)
                .toList();
    }

    public void deleteCategory(Long id) {
        findCategoryOrThrow(id);
        // İçində task olan kateqoriyanı silməyə icazə vermirik.
        if (!taskRepository.findByCategoryId(id).isEmpty()) {
            throw new ValidationException("Kateqoriyada task-lar var, silmək olmaz: id=" + id);
        }
        categoryRepository.deleteById(id);
    }

    private Category findCategoryOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Category", id));
    }
}