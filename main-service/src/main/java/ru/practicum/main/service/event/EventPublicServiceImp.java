package ru.practicum.main.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.common.dto.EndpointHitDto;
import ru.practicum.main.client.StatsClient;
import ru.practicum.main.entity.dto.event.EventFullDto;
import ru.practicum.main.entity.dto.event.EventShortDto;
import ru.practicum.main.entity.enums.EventState;
import ru.practicum.main.entity.enums.RequestStatus;
import ru.practicum.main.entity.models.Event;
import ru.practicum.main.exception.BadRequestException;
import ru.practicum.main.exception.ResourceNotFoundException;
import ru.practicum.main.mapper.EventMapper;
import ru.practicum.main.repository.EventRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс EventPublicServiceImp для отработки логики запросов и логирования
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EventPublicServiceImp implements EventPublicService {
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final EventRepository eventRepository;
    private final ProcessingEvents processingEvents;
    private final StatsClient statsClient;
    @Value("${app.name}")
    private String appName;  //private String appName = "main-service";

    /**
     * Имплементация метода получения списка событий
     *
     * @param text          Текст для поиска в содержимом аннотации и подробном описании события
     * @param categories    Список идентификаторов категорий в которых будет вестись поиск
     * @param paid          Поиск только платных/бесплатных событий
     * @param rangeStart    Дата и время не раньше которых должно произойти событие
     * @param rangeEnd      Дата и время не позже которых должно произойти событие
     * @param onlyAvailable Только события у которых не исчерпан лимит запросов на участие
     * @param sort          Вариант сортировки: по дате события или по количеству просмотров
     * @param from          Количество событий, которые нужно пропустить для формирования текущего набора
     * @param size          Количество событий в наборе
     * @return Полученный список событий
     */
    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> get(String text, List<Long> categories, Boolean paid, String rangeStart,
                                   String rangeEnd, boolean onlyAvailable, String sort,
                                   Integer from, Integer size, HttpServletRequest request) {
        checkDateTime(rangeStart == null ? null : LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)),
                rangeEnd == null ? null : LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app(appName)
                .uri("/events")
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)))
                .build();
        statsClient.createHit(endpointHitDto);
        text = text == null ? "" : text;
        List<Event> events = eventRepository.findAllByPublic(text, categories, paid, rangeStart, rangeEnd, sort, from, size);
        if (events.isEmpty()) {
            return Collections.emptyList();
        }
        List<Event> eventsAddViews = processingEvents.addViewsInEventsList(events, request);
        List<Event> newEvents = processingEvents.confirmRequests(eventsAddViews);
        log.info("Получен публичный запрос на получение всех событий");
        if (!onlyAvailable) {
            return newEvents.stream().filter(e -> e.getParticipantLimit() >= e.getConfirmedRequests())
                    .map(EventMapper::eventToEventShortDto).collect(Collectors.toList());
        }
        return newEvents.stream().map(EventMapper::eventToEventShortDto).collect(Collectors.toList());
    }

    /**
     * Имплементация метода получения подробной информации о событии по ID
     *
     * @param id ID события
     * @return Искомый объект события
     */
    @Override
    @Transactional(readOnly = true)
    public EventFullDto get(Long id, HttpServletRequest request) {
        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app(appName)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)))
                .build();
        statsClient.createHit(endpointHitDto);
        Event event = eventRepository.findEventByIdAndStateIs(id, EventState.PUBLISHED).orElseThrow(()
                -> new ResourceNotFoundException("Событие c id: " + id + " не найдено"));
        long count = processingEvents.confirmedRequestsForOneEvent(event, RequestStatus.CONFIRMED);
        event.setConfirmedRequests(count);
        long views = processingEvents.searchViews(event, request);
        event.setViews(views);
        log.info("Получен публичный запрос на получение события по id: {}", id);
        return EventMapper.eventToEventFullDto(event);
    }

    /**
     * Метод проверки даты и времени
     *
     * @param start Дата и время не раньше которых должно произойти событие
     * @param end   Дата и время не позже которых должно произойти событие
     */
    private void checkDateTime(LocalDateTime start, LocalDateTime end) {
        if (start == null) {
            start = LocalDateTime.now().minusYears(100);
        }
        if (end == null) {
            end = LocalDateTime.now();
        }
        if (start.isAfter(end)) {
            throw new BadRequestException("Некорректный запрос. Дата окончания события задана позже даты старта");
        }
    }
}