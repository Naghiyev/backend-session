package az.training.taskmanagement.repository;

import az.training.taskmanagement.model.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CategoryRepository {
    private final Map<Long, Category> storage = new HashMap<>();
    private long sequence = 0;

    public Category save(Category category) {
        Long id = category.id() == null ? ++sequence : category.id();
        Category saved = new Category(id, category.name(), category.description());
        storage.put(id, saved);
        return saved;
    }

    public Optional<Category> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Category> findAll() {
        return new ArrayList<>(storage.values());
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }
}
