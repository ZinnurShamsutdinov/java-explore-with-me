package ru.practicum.main.service.event;

import ru.practicum.main.entity.dto.EventRequestStatusUpdateRequest;
import ru.practicum.main.entity.dto.EventRequestStatusUpdateResult;
import ru.practicum.main.entity.dto.UpdateEventUserRequest;
import ru.practicum.main.entity.dto.event.EventFullDto;
import ru.practicum.main.entity.dto.event.EventShortDto;
import ru.practicum.main.entity.dto.event.NewEventDto;
import ru.practicum.main.entity.dto.request.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

//Интерфейс EventPrivateService для обработки логики запросов из EventPrivateController
public interface EventPrivateService {

    //Метод получения списка событий добавленных текущим пользователем
    List<EventShortDto> get(Long userId, int from, int size, HttpServletRequest request);

    //Метод добавления нового события
    EventFullDto create(Long userId, NewEventDto newEventDto);

    //Метод получения полной информации о событии добавленном текущим пользователем
    EventFullDto get(Long userId, Long eventId, HttpServletRequest request);

    //Метод изменения события добавленного текущим пользователем
    EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest, HttpServletRequest request);

    //Метод получения списка запросов на участие в событии текущего пользователя
    List<ParticipationRequestDto> getRequests(Long userId, Long eventId, HttpServletRequest request);

    //Метод изменения статуса (подтверждена/отменена) заявок на участие в событии текущего пользователя
    EventRequestStatusUpdateResult updateStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequest, HttpServletRequest request);
}