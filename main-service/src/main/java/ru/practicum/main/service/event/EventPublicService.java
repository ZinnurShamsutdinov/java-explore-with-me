package ru.practicum.main.service.event;

import ru.practicum.main.entity.dto.event.EventFullDto;
import ru.practicum.main.entity.dto.event.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

//Интерфейс EventPublicService для обработки логики запросов из EventPublicController
public interface EventPublicService {

     //Метод получения списка событий
    List<EventShortDto> get(String text, List<Long> categories, Boolean paid, String rangeStart, String rangeEnd,
                            boolean onlyAvailable, String sort, Integer from, Integer size, HttpServletRequest request);


     //Метод получения подробной информации о событии по ID
     EventFullDto get(Long id, HttpServletRequest request);
}