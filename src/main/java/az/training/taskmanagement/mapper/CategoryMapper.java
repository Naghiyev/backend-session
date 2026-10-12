package az.training.taskmanagement.mapper;

import az.training.taskmanagement.dto.CategoryRequest;
import az.training.taskmanagement.dto.CategoryResponse;
import az.training.taskmanagement.model.Category;

public class CategoryMapper {
    public static Category toEntity(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.name());
        return category;
    }

    public static CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }
}