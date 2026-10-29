package az.training.taskmanagement.mapper;

import az.training.taskmanagement.dto.CategoryRequest;
import az.training.taskmanagement.dto.CategoryResponse;
import az.training.taskmanagement.model.Category;

public class CategoryMapper {
    private CategoryMapper() {

    }
    public static Category toEntity(CategoryRequest request) {
        return new Category(null, request.name(), request.description());
    }
    public static CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getId(),  category.getName(), category.getDescription());
    }
}
