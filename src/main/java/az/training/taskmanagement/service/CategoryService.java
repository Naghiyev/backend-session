package az.training.taskmanagement.service;

import az.training.taskmanagement.dto.CategoryResponse;
import az.training.taskmanagement.dto.CreateCategoryReguest;
import az.training.taskmanagement.exception.DuplicateResourceException;
import az.training.taskmanagement.exception.ResourceNotFoundException;
import az.training.taskmanagement.exception.ValidationException;
import az.training.taskmanagement.mapper.CategoryMapper;
import az.training.taskmanagement.model.Category;
import az.training.taskmanagement.repository.CategoryRepository;

import java.util.List;

public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse createCategory(CreateCategoryReguest reguest){
        if(reguest.name()==null || reguest.name().isBlank()){
            throw new ValidationException("name can not be empty");
        }
        if (categoryRepository.existsByName(reguest.name())){
            throw new DuplicateResourceException("this category already exists");
        }
        Category saved=categoryRepository.save(CategoryMapper.toEntity(reguest));
        return CategoryMapper.toResponse(saved) ;
    }
    public CategoryResponse getCategoryById(Long id){
        return  CategoryMapper.toResponse(findCategoryOrThrow(id));
    }

    public List<CategoryResponse> getAllCategories(){
        return categoryRepository.findAll().stream().map(CategoryMapper::toResponse).toList();
    }
    public void deleteCategory(Long id){
        findCategoryOrThrow(id);
        categoryRepository.deleteById(id);
    }

    private Category findCategoryOrThrow(Long id){
        return categoryRepository.findById(id).orElseThrow(()-> ResourceNotFoundException.of("Category",id ));
    }
}