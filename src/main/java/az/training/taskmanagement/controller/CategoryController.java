package az.training.taskmanagement.controller;

import az.training.taskmanagement.dto.CategoryResponse;
import az.training.taskmanagement.dto.CreateCategoryReguest;
import az.training.taskmanagement.repository.CategoryRepository;
import az.training.taskmanagement.service.CategoryService;

import java.util.List;

public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //post
    public CategoryResponse create(CreateCategoryReguest reguest){
        return categoryService.createCategory(reguest);

    }
    //get/id
    public CategoryResponse getById(Long id){
        return categoryService.getCategoryById(id);
    }
    //get
    public List<CategoryResponse> getAllCategory(){
        return categoryService.getAllCategories();
    }
    //delete
    public void delete(Long id){
        categoryService.deleteCategory(id);
    }

}