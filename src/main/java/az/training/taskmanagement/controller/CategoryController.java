package az.training.taskmanagement.controller;

import az.training.taskmanagement.dto.CategoryResponse;
import az.training.taskmanagement.dto.CreateCategoryRequest;
import az.training.taskmanagement.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @PostMapping
      public ResponseEntity<CategoryResponse> create(@RequestBody CreateCategoryRequest request){
        CategoryResponse created=categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @GetMapping
    public List<CategoryResponse> getAll(){
        return categoryService.getAllCategories()   ;
    }

    @GetMapping("/{id}")
    public CategoryResponse getById(@PathVariable Long id){
        return categoryService.getById(id)   ;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete( @PathVariable  Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

}
