package ru.practicum.main.service.category;

import ru.practicum.main.entity.dto.category.CategoryDto;
import ru.practicum.main.entity.dto.category.NewCategoryDto;

//Интерфейс CategoryAdminService для обработки логики запросов из CategoryAdminController
public interface CategoryAdminService {

    //Метод добавления категории
    CategoryDto create(NewCategoryDto newCategoryDto);

    //Метод изменения категории
    CategoryDto update(Long id, CategoryDto newCategoryDto);

    //Метод удаления категории
    void delete(Long id);
}