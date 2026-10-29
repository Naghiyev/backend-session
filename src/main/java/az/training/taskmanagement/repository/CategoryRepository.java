package az.training.taskmanagement.repository;

import az.training.taskmanagement.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    Category saveCategory(Category category);
    Optional<Category> findCategoryById(Long id);

    List<Category> findAllCategories();
    List<Category> getCategoriesByName(String name);
    void deleteCategoryById(Long id);
}
