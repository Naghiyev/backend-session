//package az.training.taskmanagement.repository;
//
//import java.util.ArrayList;
//
//import java.util.List;
//
//import az.training.taskmanagement.model.Category;
//
//public class CategoryRepository {
//	List<Category> categories = new ArrayList<>();
//	public Category save(Category category) {
//	    categories.add(category);
//	    return category;
//	}
//}
package az.training.taskmanagement.repository.inMemory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import az.training.taskmanagement.model.Category;
import az.training.taskmanagement.repository.CategoryRepository;

public class InMemoryCategoryRepository implements CategoryRepository {

    private final List<Category> categories = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong(0);
    @Override
    public Category save(Category category) {

        if (category.getId() == null) {
            category.setId(sequence.incrementAndGet());
        }

        categories.add(category);

        return category;
    }
    @Override
    public Optional<Category> findById(Long id) {

        return categories.stream()
                .filter(category -> category.getId().equals(id))
                .findFirst();
    }
    @Override
    public List<Category> findAll() {
        return new ArrayList<>(categories);
    }
    @Override
    public void deleteById(Long id) {
        categories.removeIf(category -> category.getId().equals(id));
    }
}