package ru.practicum.main.controller.publicController;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.entity.dto.category.CategoryDto;
import ru.practicum.main.service.category.CategoryPublicService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

//Класс (Публичный контроллер для категорий) CategoryPublicController по энпоинту categories
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
public class CategoryPublicController {

    private final CategoryPublicService categoryPublicService;

    //Эндпоинт получения списка категории
    @GetMapping()
    public List<CategoryDto> get(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                 @Positive @RequestParam(defaultValue = "10") Integer size) {
        return categoryPublicService.get(from, size);
    }

    //Эндпоинт получения информации о категории по её ID
    @GetMapping("/{catId}")
    public CategoryDto get(@PathVariable Long catId) {
        return categoryPublicService.get(catId);
    }
}