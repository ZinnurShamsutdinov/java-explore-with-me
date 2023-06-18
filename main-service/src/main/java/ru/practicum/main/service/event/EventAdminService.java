package ru.practicum.main.service.event;

import ru.practicum.main.entity.dto.UpdateEventAdminRequest;
import ru.practicum.main.entity.dto.event.EventFullDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

//Интерфейс EventAdminService для обработки логики запросов из EventAdminController
public interface EventAdminService {

    // Метод получения списка событий
    List<EventFullDto> get(List<Long> users, List<String> states, List<Long> categories,
                           String rangeStart, String rangeEnd, int from, int size, HttpServletRequest request);

    //Метод редактирования события и его статуса (отклонение/публикация)
    EventFullDto update(Long id, UpdateEventAdminRequest updateEventAdminRequest, HttpServletRequest request);
}