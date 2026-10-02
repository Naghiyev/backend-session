package az.training.taskmanagement.repository;

import az.training.taskmanagement.model.Category;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
/**
 * In-memory Category repository.
 */
public class CategoryRepository {

    private final Map<Long, Category> storage = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);


    public Category save(Category category) {
        Category toSave = category.id() == null
                ? category.withId(sequence.incrementAndGet())
                : category;
        storage.put(toSave.id(), toSave);
        return toSave;
    }

    public Optional<Category> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Category> findAll() {
        return storage.values().stream()
                .sorted(Comparator.comparing(Category::id))
                .toList();
    }

    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }

    public boolean existsByName(String name) {
        return storage.values().stream()
                .anyMatch(c -> c.name() != null && c.name().equalsIgnoreCase(name));
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }
}
