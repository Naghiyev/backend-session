package az.training.taskmanagement.repository;

import az.training.taskmanagement.model.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class CategoryRepository {

    private final Map<Long, Category> storage = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public Category save(Category category) {
        if (category.getId() == null) {
            category.setId(sequence.incrementAndGet());
        }
        storage.put(category.getId(), category);
        return category;
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
