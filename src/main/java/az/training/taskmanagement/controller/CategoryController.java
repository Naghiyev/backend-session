package az.training.taskmanagement.controller;

import az.training.taskmanagement.dto.CategoryResponse;
import az.training.taskmanagement.dto.CreateCategoryRequest;
import az.training.taskmanagement.service.CategoryService;

import java.util.List;

public class CategoryController {

    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

//    post /categories
    public CategoryResponse create(CreateCategoryRequest request){
        return categoryService.createCategory(request);
    }
//    Get/categories{id}
    public CategoryResponse getById(Long id){
        return categoryService.getCategoryById(id);
    }
//    Get/categories
    public List<CategoryResponse> getAll(){
        return  categoryService.getAllCategories();
    }
//    Delete/categories/{id}
    public void delete(Long id){
        categoryService.deleteCategory(id);
    }


}
