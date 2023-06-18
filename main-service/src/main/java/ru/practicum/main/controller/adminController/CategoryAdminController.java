package ru.practicum.main.controller.adminController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.entity.dto.category.CategoryDto;
import ru.practicum.main.entity.dto.category.NewCategoryDto;
import ru.practicum.main.service.category.CategoryAdminService;

import javax.validation.Valid;

//Класс администрирования категорий CategoryAdminController по энпоинту admin/categories
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class CategoryAdminController {

    private final CategoryAdminService categoryAdminService;

    //Эндпоинт добавления категории
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        return categoryAdminService.create(newCategoryDto);
    }

    //Эндпоинт изменения категории
    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto update(@PathVariable Long catId, @Valid @RequestBody CategoryDto newCategoryDto) {
        return categoryAdminService.update(catId, newCategoryDto);
    }

    //Эндпоинт удаления категории
    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long catId) {
        categoryAdminService.delete(catId);
    }
}