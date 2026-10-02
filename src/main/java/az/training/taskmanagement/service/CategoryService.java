package az.training.taskmanagement.service;

import az.training.taskmanagement.model.Category;
import az.training.taskmanagement.repository.CategoryRepository;

import java.util.List;

public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(String name){
        if(name== null || name.isBlank()){
            throw  new IllegalArgumentException("name can not be empty!");
        }
        if(categoryRepository.exitsByName(name)){
            throw new IllegalArgumentException("this Category already exists " + name );
        }
        return categoryRepository.save(new Category(null, name));

    }

    public Category getCategoryById(Long id){
        return  categoryRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("CAtegory not found "));
    }

    public List<Category> getAllCategory(){
        return categoryRepository.findAll();
    }
    public void deleteCategory(Long id){
        getCategoryById(id);
        categoryRepository.deleteById(id);
    }
}
