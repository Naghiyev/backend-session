package az.training.taskmanagement.controller;

import az.training.taskmanagement.dto.CategoryResponse;
import az.training.taskmanagement.dto.CreateCategoryRequest;
import az.training.taskmanagement.dto.UpdateCategoryRequest;
import az.training.taskmanagement.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService service;
    public CategoryController(CategoryService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse create(@RequestBody CreateCategoryRequest request) {
        return service.createCategory(request);
    }
    @GetMapping
    public List<CategoryResponse> getAll() { return service.getAllCategories(); }
    @GetMapping("/{id}")
    public CategoryResponse getById(@PathVariable("id") Long id) { return service.getCategoryById(id); }
    @PatchMapping("/{id}")
    public CategoryResponse update(@PathVariable("id") Long id, @RequestBody UpdateCategoryRequest request) {
        return service.updateCategory(id, request);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) { service.deleteCategory(id); }
}
