package az.training.taskmanagement.repository;

import az.training.taskmanagement.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    public Category save(Category category);

    public boolean existsByNameExceptId(String name, Long id);

    public Optional<Category> findById(Long id);

    public List<Category> findAll();

    public void deleteById(Long id);

}
