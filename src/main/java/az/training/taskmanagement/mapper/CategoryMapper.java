package az.training.taskmanagement.mapper;

import az.training.taskmanagement.dto.CategoryResponse;
import az.training.taskmanagement.dto.CreateCategoryRequest;
import az.training.taskmanagement.model.Category;

public class CategoryMapper {
    private CategoryMapper(){}

    public static Category toEntity(CreateCategoryRequest request){return new Category(null, request.name());
    }
    public static CategoryResponse toResponse(Category category){
        return new CategoryResponse(category.getId(), category.getName());
    }
}