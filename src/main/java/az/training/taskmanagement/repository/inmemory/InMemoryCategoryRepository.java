package az.training.taskmanagement.repository.inmemory;

import az.training.taskmanagement.model.Category;
import az.training.taskmanagement.repository.CategoryRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryCategoryRepository implements CategoryRepository {
    private final Map<Long, Category> storage = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    public Category save(Category category) {
        if (category.getId() == null) category.setId(sequence.incrementAndGet());
        storage.put(category.getId(), copy(category));
        return copy(category);
    }
    public Optional<Category> findById(Long id) {
        return id == null ? Optional.empty() : Optional.ofNullable(storage.get(id)).map(InMemoryCategoryRepository::copy);
    }
    public List<Category> findAll() {
        return storage.values().stream().sorted(Comparator.comparing(Category::getId))
                .map(InMemoryCategoryRepository::copy).toList();
    }
    public void deleteById(Long id) { storage.remove(id); }
    private static Category copy(Category category) {
        return new Category(category.getId(), category.getName());
    }
}
