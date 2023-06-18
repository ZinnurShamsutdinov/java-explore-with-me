package ru.practicum.main.service.category;

import ru.practicum.main.entity.dto.category.CategoryDto;

import java.util.List;

//Интерфейс CategoryPublicService для обработки логики запросов из CategoryPublicController
public interface CategoryPublicService {


    //Метод получения списка категории
    List<CategoryDto> get(int from, int size);

    //Метод получения информации о категории по её ID
    CategoryDto get(Long catId);
}