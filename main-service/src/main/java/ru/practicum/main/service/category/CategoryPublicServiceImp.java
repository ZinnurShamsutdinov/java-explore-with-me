package ru.practicum.main.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.entity.dto.category.CategoryDto;
import ru.practicum.main.entity.models.Category;
import ru.practicum.main.mapper.CategoryMapper;
import ru.practicum.main.repository.CategoryRepository;
import ru.practicum.main.service.FindObjectInService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс CategoryPublicServiceImp для отработки логики запросов и логирования
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryPublicServiceImp implements CategoryPublicService {

    private final CategoryRepository categoryRepository;
    private final FindObjectInService findObjectInService;

    /**
     * Имплементация метода получения списка категории
     *
     * @param from количество категорий, которые нужно пропустить для формирования текущего набора
     * @param size количество категорий в наборе
     * @return Список категорий
     */
    @Override
    public List<CategoryDto> get(int from, int size) {
        log.info("Получен запрос на список всех категорий");
        return categoryRepository.findAll(PageRequest.of(from, size)).stream()
                .map(CategoryMapper::categoryToCategoryDto).collect(Collectors.toList());
    }

    /**
     * Имплементация метода получения информации о категории по её ID
     *
     * @param id содержит данные catId ID категории
     * @return Объект CategoryDto
     */
    @Override
    public CategoryDto get(Long id) {
        Category category = findObjectInService.getCategoryById(id);
        log.info("Получен запрос на поиск категории по id: {}", id);
        return CategoryMapper.categoryToCategoryDto(category);
    }
}