package ru.practicum.main.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main.entity.dto.category.CategoryDto;
import ru.practicum.main.entity.dto.category.NewCategoryDto;
import ru.practicum.main.entity.models.Category;

//Утилитарный класс CategoryMapper для преобразования Category / CategoryDto / newCategoryDto
@UtilityClass
public class CategoryMapper {

    //Преобразование CategoryDto в Category
    public Category categoryDtoToCategory(CategoryDto categoryDto) {
        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .build();
    }

    //Преобразование Category в CategoryDto
    public CategoryDto categoryToCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    //Преобразование NewCategoryDto в Category
    public Category newCategoryDtoToCategory(NewCategoryDto newCategoryDto) {
        return Category.builder()
                .name(newCategoryDto.getName())
                .build();
    }
}