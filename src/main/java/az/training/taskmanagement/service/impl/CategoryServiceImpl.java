package az.training.taskmanagement.service.impl;

import az.training.taskmanagement.dto.CategoryResponse;
import az.training.taskmanagement.dto.CreateCategoryRequest;
import az.training.taskmanagement.exception.ResourceNotFoundException;
import az.training.taskmanagement.mapper.CategoryMapper;
import az.training.taskmanagement.model.Category;
import az.training.taskmanagement.repository.CategoryRepository;
import az.training.taskmanagement.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;


@org.springframework.stereotype.Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    @Override
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        if (request.name() == null || request.name().isBlank()) {
            throw new IllegalArgumentException("Category name boş ola bilməz");
        }
        Category saved = categoryRepository.save(CategoryMapper.toEntity(request));
        return CategoryMapper.toResponse(saved);
    }

    public CategoryResponse getCategoryById(Long id) {

        return CategoryMapper.toResponse(findCategoryOrThrow(id));
    }

    public List<CategoryResponse> getAllOrByName(String name) {
        return categoryRepository.findAll().stream()
                .filter(c -> name == null || name.isBlank() || c.getName().toLowerCase()
                        .contains(name.toLowerCase()))
                .map(CategoryMapper::toResponse).toList();
    }

    public void deleteCategory(Long id) {
        findCategoryOrThrow(id);
        categoryRepository.deleteById(id);
    }



    private Category findCategoryOrThrow(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> ResourceNotFoundException.of("category", id));
    }

}