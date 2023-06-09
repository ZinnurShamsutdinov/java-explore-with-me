package ru.practicum.main.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.entity.dto.category.CategoryDto;
import ru.practicum.main.entity.dto.category.NewCategoryDto;
import ru.practicum.main.entity.models.Category;
import ru.practicum.main.exception.ConflictDeleteException;
import ru.practicum.main.exception.ConflictNameCategoryException;
import ru.practicum.main.mapper.CategoryMapper;
import ru.practicum.main.repository.CategoryRepository;
import ru.practicum.main.service.FindObjectInService;

/**
 * Класс CategoryAdminServiceImp для отработки логики запросов и логирования
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryAdminServiceImp implements CategoryAdminService {

    private final CategoryRepository categoryRepository;
    private final FindObjectInService findObjectInService;

    /**
     * Имплементация метода добавления категории
     *
     * @param newCategoryDto Новая категория в объекте NewCategoryDto
     * @return добавленная CategoryDto
     */
    @Override
    @Transactional
    public CategoryDto create(NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.newCategoryDtoToCategory(newCategoryDto);
        log.info("Получен запрос на добавление категории с названием: {}", newCategoryDto.getName());
        checkNameCategory(category.getName());
        return CategoryMapper.categoryToCategoryDto(categoryRepository.save(category));
    }

    /**
     * Имплементация метода изменения категории
     *
     * @param id             ID категории
     * @param categoryDto содержит данные объекта NewCategoryDto
     * @return изменённая CategoryDto
     */
    @Override
    @Transactional
    public CategoryDto update(Long id, CategoryDto categoryDto) {
        Category category = findObjectInService.getCategoryById(id);
        if (!categoryDto.getName().equals(category.getName())) {
            checkNameCategory(categoryDto.getName());
            category.setName(categoryDto.getName());
        }
        log.info("Получен запрос на обновлении категории c id: {}", id);
        return CategoryMapper.categoryToCategoryDto(categoryRepository.save(category));
    }

    /**
     * Имплементация метода удаления категории
     *
     * @param id ID категории
     */
    @Override
    @Transactional
    public void delete(Long id) {
        Category category = findObjectInService.getCategoryById(id);
        if (findObjectInService.isRelatedEvent(category)) {
            throw new ConflictDeleteException("Существуют события, связанные с категорией " + category.getName());
        }
        log.info("Получен запрос на удаление категории c id: {}", id);
        categoryRepository.delete(category);
    }

    /**
     * Метод проверки категории на дубликат
     *
     * @param name Название категории
     */
    private void checkNameCategory(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new ConflictNameCategoryException("Имя категории " + name + " уже есть в базе");
        }
    }
}