package ru.practicum.main.controller.publicController;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.entity.dto.event.EventFullDto;
import ru.practicum.main.entity.dto.event.EventShortDto;
import ru.practicum.main.service.event.EventPublicService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

//Класс (Публичный контроллер для работы с событиями) EventPublicController по энпоинту events
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class EventPublicController {

    private final EventPublicService eventPublicService;

    //Эндпоинт получения списка событий
    @GetMapping()
    List<EventShortDto> get(@RequestParam(required = false) String text,
                            @RequestParam(required = false) List<Long> categories,
                            @RequestParam(required = false) Boolean paid,
                            @RequestParam(required = false) String rangeStart,
                            @RequestParam(required = false) String rangeEnd,
                            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                            @RequestParam(defaultValue = "EVENT_DATE") String sort,
                            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                            @Positive @RequestParam(defaultValue = "10") Integer size,
                            HttpServletRequest request) {
        return eventPublicService.get(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size, request);
    }

    //Эндпоинт получения подробной информации о событии по ID
    @GetMapping("/{id}")
    EventFullDto get(@PathVariable Long id, HttpServletRequest request) {
        return eventPublicService.get(id, request);
    }
}