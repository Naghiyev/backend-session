package az.training.taskmanagement.service.impl;

import az.training.taskmanagement.dto.CategoryResponse;
import az.training.taskmanagement.dto.CreateCategoryRequest;
import az.training.taskmanagement.exception.ResourceNotFoundException;
import az.training.taskmanagement.exception.ValidationException;
import az.training.taskmanagement.mapper.CategoryMapper;
import az.training.taskmanagement.model.Category;
import az.training.taskmanagement.repository.CategoryRepository;
import az.training.taskmanagement.service.CategoryService;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository ;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryResponse createCategory(CreateCategoryRequest request) {
            if(request.name()==null || request.name().isBlank()){
                throw new ValidationException("Category can not be empty");

            }
            if(categoryRepository.existsByName(request.name())){
                throw new ValidationException("Category already exists");
            }
       Category saved= CategoryRepository.save(CategoryMapper.toEntity(request));
        return CategoryMapper.toResponse(saved);
    }

    @Override
    public CategoryResponse getById(Long id) {
        return  CategoryMapper.toResponse(findOrThrow(id));

    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream().map(CategoryMapper::toResponse).toList();
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
    private Category findOrThrow(Long id){
        return categoryRepository.findById(id).orElseThrow(()->  ResourceNotFoundException.of("category", id));
    }
}
