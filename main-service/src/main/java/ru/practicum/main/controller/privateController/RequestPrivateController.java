package ru.practicum.main.controller.privateController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.entity.dto.request.ParticipationRequestDto;
import ru.practicum.main.service.request.RequestPrivateService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;

//Класс (Закрытый контроллер для работы с запросами) RequestPrivateController по энпоинту users/{userId}/requests
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")

public class RequestPrivateController {

    private final RequestPrivateService requestPrivateService;


    //Эндпоинт получения информации о заявках текущего пользователя на участие в чужих событиях
    @GetMapping
    List<ParticipationRequestDto> get(@PathVariable Long userId) {
        return requestPrivateService.get(userId);
    }

    //Эндпоинт добавление запроса от текущего пользователя на участие в событии
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ParticipationRequestDto create(@PathVariable Long userId,
                                   @NotNull @RequestParam Long eventId,
                                   HttpServletRequest request) {
        return requestPrivateService.create(userId, eventId, request);
    }

    //Эндпоинт отмены своего запроса на участие в событии
    @PatchMapping("/{requestId}/cancel")
    ParticipationRequestDto update(@PathVariable Long userId,
                                   @PathVariable Long requestId,
                                   HttpServletRequest request) {
        return requestPrivateService.update(userId, requestId, request);
    }
}