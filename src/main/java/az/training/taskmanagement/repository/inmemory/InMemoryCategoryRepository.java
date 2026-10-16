package az.training.taskmanagement.repository.inmemory;

import az.training.taskmanagement.model.Category;
import az.training.taskmanagement.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@org.springframework.stereotype.Repository
public class InMemoryCategoryRepository implements CategoryRepository {
    private final Map<Long, Category> storage = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    @Override
    public Category save(Category category) {
        if (category.id() == null) {
            category = new Category(sequence.incrementAndGet(), category.name());
        }
        storage.put(category.id(), category);
        return category;
    }

    @Override
    public boolean existsByNameExceptId(String name, Long id) {
        return storage.values().stream()
                .anyMatch(c -> c.id() != id && c.name().equalsIgnoreCase(name));
    }

    @Override
    public Optional<Category> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Category> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }
}