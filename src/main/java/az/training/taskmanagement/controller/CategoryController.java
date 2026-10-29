package az.training.taskmanagement.controller;

import az.training.taskmanagement.dto.CategoryResponse;
import az.training.taskmanagement.dto.CreateCategoryRequest;
import az.training.taskmanagement.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //    post /categories
    @PostMapping
    public ResponseEntity<CategoryResponse> create(@RequestBody CreateCategoryRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(request)) ;
    }
    //    Get/categories{id}

    @GetMapping("/{id}")
    public CategoryResponse getById(@PathVariable Long id){
        return categoryService.getCategoryById(id);
    }
    //    Get/categories
    @GetMapping
    public List<CategoryResponse> getAllOrByName(@RequestParam(required = false) String name){
        return  categoryService.getAllOrByName(name);
    }
    //    Delete/categories/{id}
    public void delete(Long id){
        categoryService.deleteCategory(id);
    }



}