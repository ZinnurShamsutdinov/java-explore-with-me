package ru.practicum.main.service.category;

import ru.practicum.main.entity.dto.category.CategoryDto;
import ru.practicum.main.entity.dto.category.NewCategoryDto;

/**
 * Интерфейс CategoryAdminService для обработки логики запросов из CategoryAdminController
 */
public interface CategoryAdminService {

    /**
     * Метод добавления категории
     *
     * @param newCategoryDto Новая категория в объекте NewCategoryDto
     * @return добавленная CategoryDto
     */
    CategoryDto create(NewCategoryDto newCategoryDto);

    /**
     * Метод изменения категории
     *
     * @param id             ID категории
     * @param newCategoryDto категория в объекте NewCategoryDto
     * @return изменённая CategoryDto
     */
    CategoryDto update(Long id, CategoryDto newCategoryDto);

    /**
     * Метод  удаления категории
     *
     * @param id ID категории
     */
    void delete(Long id);
}