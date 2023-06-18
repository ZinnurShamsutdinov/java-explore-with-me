package ru.practicum.main.entity.dto.compilation;

import lombok.Builder;
import lombok.Value;
import ru.practicum.main.entity.dto.event.EventShortDto;

import java.util.List;

//Модель объекта Compilation Data Transfer Object Подборка событий
@Value
@Builder
public class CompilationDto {
    List<EventShortDto> events;
    Long id;
    boolean pinned;
    String title;
}