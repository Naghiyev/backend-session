package az.training.taskmanagement.repository;

import az.training.taskmanagement.model.Category;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class CategoryRepository {

    private final List<Category> categories = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Category save(Category category) {
        if (category.getId() == null) {
            category.setId(idGenerator.getAndIncrement());
        }
        categories.add(category);
        return category;
    }

    public Optional<Category> findById(Long id) {
        return categories.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    public List<Category> findAll() {
        return new ArrayList<>(categories);
    }
}