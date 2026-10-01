package az.training.taskmanagement.repository;

import az.training.taskmanagement.model.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class CategoryRepository {
    public static List<Category> categories = new ArrayList<>();
    private static final AtomicInteger sequence = new AtomicInteger(0);

    public Category save(Category category) {
        if (category.getId() == null) {
            category.setId((long) sequence.incrementAndGet());
        }
        categories.add(category);
        return category;
    }

    public List<Category> findAll() {
        return categories;
    }

    public Optional<Category> findById(Long categoryId) {
        return categories.stream()
                .filter(category -> category.getId().equals(categoryId))
                .findFirst();
    }

    public boolean existsById(Long categoryId) {
        if (categoryId == null) {
            return false;
        }
        return categories.stream()
                .anyMatch(category -> category.getId().equals(categoryId));
    }
}
