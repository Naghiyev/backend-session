package az.training.taskmanagement.mapper;

import az.training.taskmanagement.dto.CategoryResponse;
import az.training.taskmanagement.dto.CreateCategoryReguest;
import az.training.taskmanagement.model.Category;

public final class CategoryMapper{
    private CategoryMapper(){

    }
    public static Category toEntity(CreateCategoryReguest reguest){
        return new Category(null , reguest.name());
    }
    public static CategoryResponse toResponse(Category category){
        return new CategoryResponse(
                category.getId(), category.getName()
        );
    }

}