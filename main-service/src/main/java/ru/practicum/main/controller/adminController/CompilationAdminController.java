package ru.practicum.main.controller.adminController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.entity.dto.UpdateCompilationRequest;
import ru.practicum.main.entity.dto.compilation.CompilationDto;
import ru.practicum.main.entity.dto.compilation.NewCompilationDto;
import ru.practicum.main.service.compilation.CompilationAdminService;

//Класс администрирования подборок событий CompilationAdminController по энпоинту admin/compilations
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
public class CompilationAdminController {
    private final CompilationAdminService compilationAdminService;


    //Эндпоинт создания подборки
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto create(@Validated @RequestBody NewCompilationDto newCompilationDto) {
        return compilationAdminService.create(newCompilationDto);
    }

    //Эндпоинт обновления подборки по ID
    @PatchMapping("/{compId}")
    public CompilationDto update(@PathVariable Long compId,
                                 @Validated @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        return compilationAdminService.update(compId, updateCompilationRequest);
    }

    //Эндпоинт удаления подборки по ID
    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long compId) {
        compilationAdminService.delete(compId);
    }
}