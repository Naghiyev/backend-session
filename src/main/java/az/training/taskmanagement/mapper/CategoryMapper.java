package az.training.taskmanagement.mapper;

import az.training.taskmanagement.dto.category.request.CreateCategoryRequest;
import az.training.taskmanagement.dto.category.response.CategoryResponse;
import az.training.taskmanagement.model.Category;

public final class CategoryMapper {

    private CategoryMapper() {
    }

    public static Category toEntity(CreateCategoryRequest request) {
        return new Category(null, request.name(), request.description());
    }

    public static CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getDescription());
    }
}
