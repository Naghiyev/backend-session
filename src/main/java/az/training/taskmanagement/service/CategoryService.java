package az.training.taskmanagement.service;

import az.training.taskmanagement.exception.CategoryNotFoundException;
import az.training.taskmanagement.exception.DuplicateCategoryException;
import az.training.taskmanagement.exception.ResourceInUseException;
import az.training.taskmanagement.model.Category;
import az.training.taskmanagement.repository.CategoryRepository;
import az.training.taskmanagement.repository.TaskRepository;

import java.util.List;

import static az.training.taskmanagement.util.ValidationUtils.requireNonBlank;
import static az.training.taskmanagement.util.ValidationUtils.requireNonNull;

/**
 * Category üçün business logic.
 *
 * Qaydalar:
 *  - name boş ola bilməz və unikal olmalıdır (böyük/kiçik hərf fərqi nəzərə alınmır)
 *  - task-ları olan category silinə bilməz
 */
public class CategoryService {

    private static final int NAME_MAX_LENGTH = 50;

    private final CategoryRepository categoryRepository;
    private final TaskRepository taskRepository;

    public CategoryService(CategoryRepository categoryRepository, TaskRepository taskRepository) {
        this.categoryRepository = categoryRepository;
        this.taskRepository = taskRepository;
    }

    public Category createCategory(String name, String description) {
        String validName = requireNonBlank(name, "name", NAME_MAX_LENGTH);
        if (categoryRepository.existsByName(validName)) {
            throw new DuplicateCategoryException(validName);
        }
        return categoryRepository.save(new Category(null, validName, description));
    }

    public Category getCategoryById(Long id) {
        requireNonNull(id, "id");
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category updateCategory(Long id, String name, String description) {
        Category category = getCategoryById(id);
        String validName = requireNonBlank(name, "name", NAME_MAX_LENGTH);
        if (!category.name().equalsIgnoreCase(validName)
                && categoryRepository.existsByName(validName)) {
            throw new DuplicateCategoryException(validName);
        }
        return categoryRepository.save(new Category(category.id(), validName, description));
    }

    public void deleteCategory(Long id) {
        getCategoryById(id);
        if (taskRepository.existsByCategoryId(id)) {
            throw new ResourceInUseException(
                    "Category silinə bilməz, ona aid task-lar var: id=" + id);
        }
        categoryRepository.deleteById(id);
    }
}
