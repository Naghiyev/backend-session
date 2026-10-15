package az.training.taskmanagement.repository.inmemory;

import az.training.taskmanagement.model.Category;
import az.training.taskmanagement.repository.CategoryRepository;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryCategoryRepository implements CategoryRepository {

    private final Map<Long, Category> storage = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    @Override
    public Category save(Category category) {
        Category toSave = category.id() == null
                ? category.withId(sequence.incrementAndGet())
                : category;
        storage.put(toSave.id(), toSave);
        return toSave;
    }

    @Override
    public Optional<Category> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Category> findAll() {
        return storage.values().stream()
                .sorted(Comparator.comparing(Category::id))
                .toList();
    }

    @Override
    public boolean existsByName(String name) {
        return storage.values().stream()
                .anyMatch(c -> c.name().equalsIgnoreCase(name));
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }
}
