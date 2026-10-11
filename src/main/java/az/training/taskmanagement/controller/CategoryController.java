package az.training.taskmanagement.controller;

import az.training.taskmanagement.dto.CategoryResponse;
import az.training.taskmanagement.dto.CreateCategoryRequest;
import az.training.taskmanagement.dto.TaskResponse;
import az.training.taskmanagement.service.CategoryService;
import az.training.taskmanagement.service.TaskService;

import java.util.List;

public class CategoryController {

    private final CategoryService categoryService;
    private final TaskService taskService;
    public CategoryController(CategoryService categoryService , TaskService taskService) {
        this.categoryService = categoryService;
        this.taskService = taskService;
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
    //    Get/categories/{id}/tasks
    public List<TaskResponse> getCategoryTasks(Long id){
        return taskService.getTasksByCategory(id);
    }
    //    Delete/categories/{id}
    public void delete(Long id){
        categoryService.deleteCategory(id);
    }


}