package az.training.taskmanagement.repository;

import az.training.taskmanagement.model.Category;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class CategoryRepository {
    private final Map<Long, Category> categories=new ConcurrentHashMap<>();
    private final AtomicLong idGenerator=new AtomicLong(1);

    public Category save(Category category) {
        if (category.getId()==null) {
            category.setId(idGenerator.getAndIncrement());
        }
        categories.put(category.getId(), category);
        return category;
    }
    public Category findById(Long id) {
        return categories.get(id);
    }
    public Map<Long, Category> findAll() {
        return categories;
    }
    public void deleteById(Long id) {
        categories.remove(id);
    }
}
