package ru.practicum.main.service.compilation;

import ru.practicum.main.entity.dto.compilation.CompilationDto;

import java.util.List;

//Интерфейс CompilationPublicService для обработки логики запросов из CompilationPublicController
public interface CompilationPublicService {

    // Метод получения списка подборок событий
    List<CompilationDto> get(Boolean pinned, int from, int size);

    //Метод получения подборки событий по ID
    CompilationDto get(Long id);
}