package ru.practicum.main.service.compilation;

import ru.practicum.main.entity.dto.UpdateCompilationRequest;
import ru.practicum.main.entity.dto.compilation.CompilationDto;
import ru.practicum.main.entity.dto.compilation.NewCompilationDto;

/**
 * Интерфейс CompilationAdminService для обработки логики запросов из CompilationAdminController
 */
public interface CompilationAdminService {

    /**
     * Метод создания подборки
     *
     * @param newCompilationDto Объект NewCompilationDto
     * @return Созданный объект подборки NewCompilationDto
     */
    CompilationDto create(NewCompilationDto newCompilationDto);

    /**
     * Метод обновления подборки по ID
     *
     * @param compId                   ID подборки
     * @param updateCompilationRequest Объект подборки UpdateCompilationRequest
     * @return Изменённый объект подборки CompilationDto
     */
    CompilationDto update(Long compId, UpdateCompilationRequest updateCompilationRequest);

    /**
     * Метод удаления подборки по ID
     *
     * @param compId ID подборки
     */
    void delete(Long compId);
}