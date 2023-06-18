package ru.practicum.main.service.compilation;

import ru.practicum.main.entity.dto.UpdateCompilationRequest;
import ru.practicum.main.entity.dto.compilation.CompilationDto;
import ru.practicum.main.entity.dto.compilation.NewCompilationDto;

//Интерфейс CompilationAdminService для обработки логики запросов из CompilationAdminController
public interface CompilationAdminService {

    //Метод создания подборки
    CompilationDto create(NewCompilationDto newCompilationDto);

    //Метод обновления подборки по ID
    CompilationDto update(Long compId, UpdateCompilationRequest updateCompilationRequest);

    //Метод удаления подборки по ID
    void delete(Long compId);
}