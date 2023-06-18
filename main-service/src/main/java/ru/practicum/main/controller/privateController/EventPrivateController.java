package ru.practicum.main.controller.privateController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.entity.dto.EventRequestStatusUpdateRequest;
import ru.practicum.main.entity.dto.EventRequestStatusUpdateResult;
import ru.practicum.main.entity.dto.UpdateEventUserRequest;
import ru.practicum.main.entity.dto.event.EventFullDto;
import ru.practicum.main.entity.dto.event.EventShortDto;
import ru.practicum.main.entity.dto.event.NewEventDto;
import ru.practicum.main.entity.dto.request.ParticipationRequestDto;
import ru.practicum.main.service.event.EventPrivateService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

//Класс (Закрытый контроллер для работы с событиями) EventPrivateController по энпоинту users/{userId}/events
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class EventPrivateController {

    private final EventPrivateService eventPrivateService;

    //Эндпоинт получения списка событий добавленных текущим пользователем
    @GetMapping()
    List<EventShortDto> get(@PathVariable Long userId,
                            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                            @Positive @RequestParam(defaultValue = "10") Integer size,
                            HttpServletRequest request) {
        return eventPrivateService.get(userId, from, size, request);
    }

    //Эндпоинт добавления нового события
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    EventFullDto create(@PathVariable Long userId,
                        @Validated @RequestBody NewEventDto newEventDto) {
        return eventPrivateService.create(userId, newEventDto);
    }

    //Эндпоинт получения полной информации о событии добавленном текущим пользователем
    @GetMapping("/{eventId}")
    EventFullDto get(@PathVariable Long userId,
                     @PathVariable Long eventId,
                     HttpServletRequest request) {
        return eventPrivateService.get(userId, eventId, request);
    }

    //Эндпоинт изменения события добавленного текущим пользователем
    @PatchMapping("/{eventId}")
    EventFullDto update(@PathVariable Long userId,
                        @PathVariable Long eventId,
                        @Validated @RequestBody UpdateEventUserRequest updateEventUserRequest,
                        HttpServletRequest request) {
        return eventPrivateService.update(userId, eventId, updateEventUserRequest, request);
    }

    //Эндпоинт получения списка запросов на участие в событии текущего пользователя
    @GetMapping("/{eventId}/requests")
    List<ParticipationRequestDto> getRequests(@PathVariable Long userId,
                                              @PathVariable Long eventId,
                                              HttpServletRequest request) {
        return eventPrivateService.getRequests(userId, eventId, request);
    }

    //Эндпоинт изменения статуса (подтверждена/отменена) заявок на участие в событии текущего пользователя
    @PatchMapping("/{eventId}/requests")
    EventRequestStatusUpdateResult updateStatus(@PathVariable Long userId,
                                                @PathVariable Long eventId,
                                                @RequestBody EventRequestStatusUpdateRequest eventRequest,
                                                HttpServletRequest request) {
        return eventPrivateService.updateStatus(userId, eventId, eventRequest, request);
    }
}