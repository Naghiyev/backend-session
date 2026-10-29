package az.training.taskmanagement.repository.inmemory;

import az.training.taskmanagement.model.Category;
import az.training.taskmanagement.repository.CategoryRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
@org.springframework.stereotype.Repository
public class InMemoryCategoryRepository implements CategoryRepository {

    private final Map<Long, Category> categories=new ConcurrentHashMap<>();
    private final AtomicLong sequence=new AtomicLong(0);

    @Override
    public Category saveCategory(Category category) {
        if (category.getId() == null) {
            category.setId(sequence.incrementAndGet());
        }
        categories.put(category.getId(), category);
        return category;
    }

    @Override
    public Optional<Category> findCategoryById(Long id) {
        return Optional.ofNullable(categories.get(id));
    }

    @Override
    public List<Category> findAllCategories() {
        return new ArrayList<>(categories.values());
    }

    @Override
    public List<Category> getCategoriesByName(String name) {
        List<Category> result=new ArrayList<>();
        for (Category category : findAllCategories()) {
            if (category.getName() !=null && category.getName().equals(name)) {
                result.add(category);
            }
        }
        return result;
    }

    @Override
    public void deleteCategoryById(Long id) {
        categories.remove(id);

    }
}
