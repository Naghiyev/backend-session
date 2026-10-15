package az.training.taskmanagement.mapper;

import az.training.taskmanagement.dto.CategoryResponse;
import az.training.taskmanagement.dto.CreateCategoryRequest;
import az.training.taskmanagement.dto.UpdateCategoryRequest;
import az.training.taskmanagement.model.Category;

public final class CategoryMapper {

    private CategoryMapper() {
    }

    public static Category toEntity(CreateCategoryRequest request) {
        return new Category(null, request.name().trim(), request.description());
    }

    public static Category toEntity(Long id, UpdateCategoryRequest request) {
        return new Category(id, request.name().trim(), request.description());
    }

    public static CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.id(), category.name(), category.description());
    }
}