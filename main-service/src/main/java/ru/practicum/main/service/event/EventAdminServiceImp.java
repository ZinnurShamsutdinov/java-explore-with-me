package ru.practicum.main.service.event;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.entity.dto.UpdateEventAdminRequest;
import ru.practicum.main.entity.dto.event.EventFullDto;
import ru.practicum.main.entity.enums.ActionState;
import ru.practicum.main.entity.enums.EventState;
import ru.practicum.main.entity.models.Category;
import ru.practicum.main.entity.models.Event;
import ru.practicum.main.exception.BadRequestException;
import ru.practicum.main.exception.ForbiddenEventException;
import ru.practicum.main.exception.ResourceNotFoundException;
import ru.practicum.main.mapper.EventMapper;
import ru.practicum.main.mapper.LocationMapper;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.service.FindObjectInService;
import ru.practicum.main.utilities.DateFormatter;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.main.service.event.EventPrivateServiceImp.getEventStateDeterming;

/**
 * Класс EventAdminServiceImp имплементация интерфеса EventAdminService для отработки логики запросов и логирования
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EventAdminServiceImp implements EventAdminService {

    private final EventRepository eventRepository;
    private final FindObjectInService findObjectInService;
    private final ProcessingEvents processingEvents;

    /**
     * Имплементация метода получения списка событий
     *
     * @param users      Список id пользователей, чьи события нужно найти
     * @param states     Список состояний в которых находятся искомые события
     * @param categories Список id категорий в которых будет вестись поиск
     * @param rangeStart Дата и время не раньше которых должно произойти событие
     * @param rangeEnd   Дата и время не позже которых должно произойти событие
     * @param from       Количество событий, которые нужно пропустить для формирования текущего набора
     * @param size       Количество событий в наборе
     * @return Полученный список событий
     */
    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> get(List<Long> users, List<String> states, List<Long> categories,
                                  String rangeStart, String rangeEnd, int from, int size, HttpServletRequest request) {
        PageRequest page = PageRequest.of(from, size);
        List<Event> events;
        LocalDateTime newRangeStart = null;
        if (rangeStart != null) {
            newRangeStart = DateFormatter.formatDate(rangeStart);
        }
        LocalDateTime newRangeEnd = null;
        if (rangeEnd != null) {
            newRangeEnd = DateFormatter.formatDate(rangeEnd);
        }
        log.info("Получен запрос от администратора на поиск событий");
        if (states != null) {
            events = eventRepository.findAllByAdmin(users, states, categories, newRangeStart, newRangeEnd, from, size);
            List<Event> eventsAddViews = processingEvents.addViewsInEventsList(events, request);
            List<Event> newEvents = processingEvents.confirmRequests(eventsAddViews);
            return newEvents.stream().map(EventMapper::eventToEventFullDto).collect(Collectors.toList());
        } else {
            events = eventRepository.findAllByAdminAndState(users, null, categories, newRangeStart, newRangeEnd, page);
            List<Event> eventsAddViews = processingEvents.addViewsInEventsList(events, request);
            List<Event> newEvents = processingEvents.confirmRequests(eventsAddViews);
            return newEvents.stream().map(EventMapper::eventToEventFullDto).collect(Collectors.toList());
        }
    }

    /**
     * Метод редактирования события и его статуса (отклонение/публикация)
     *
     * @param eventId     содержит ID события
     * @param updateEvent содержит данные объекта UpdateEventAdminRequest с изменениями
     * @return воззвращает тредактированный объект передачи данных EventFullDto
     */
    @Override
    @Transactional
    public EventFullDto update(Long eventId, UpdateEventAdminRequest updateEvent, HttpServletRequest request) {
        Event event = findObjectInService.getEventById(eventId);
        eventAvailability(event);
        if (updateEvent.getEventDate() != null) {
            checkEventDate(DateFormatter.formatDate(updateEvent.getEventDate()));
        }
        if (updateEvent.getAnnotation() != null && !updateEvent.getAnnotation().isBlank()) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            Category category = findObjectInService.getCategoryById(updateEvent.getCategory());
            event.setCategory(category);
        }
        if (updateEvent.getDescription() != null && !updateEvent.getDescription().isBlank()) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getEventDate() != null) {
            event.setEventDate(DateFormatter.formatDate(updateEvent.getEventDate()));
        }
        if (updateEvent.getLocation() != null) {
            event.setLocation(LocationMapper.locationDtoToLocation(updateEvent.getLocation()));
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getRequestModeration() != null) {
            event.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (updateEvent.getStateAction() != null) {
            if (!event.getState().equals(EventState.PUBLISHED) && updateEvent.getStateAction().equals(ActionState.PUBLISH_EVENT)) {
                event.setPublishedOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            } else if (event.getPublishedOn() == null) {
                event.setPublishedOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)); //????
            }
            event.setState(determiningTheStatusForEvent(updateEvent.getStateAction()));
        }
        if (updateEvent.getTitle() != null && !updateEvent.getTitle().isBlank()) {
            event.setTitle(updateEvent.getTitle());
        }
        if (event.getState().equals(EventState.PUBLISHED)) {
            long views = processingEvents.searchViews(event, request);
            event.setViews(views);
        } else {
            event.setViews(0L);
            event.setConfirmedRequests(0L);
        }
        try {
            log.info("Получен запрос от администратора на обновление события с id: {}", eventId);
            return EventMapper.eventToEventFullDto(eventRepository.save(event));
        } catch (DataAccessException e) {
            throw new ResourceNotFoundException("База данных недоступна");
        } catch (Exception e) {
            throw new BadRequestException("Запрос на добавлении события " + event + " составлен не корректно ");
        }
    }

    /**
     * Метод проверки времени и даты от текущего времени
     *
     * @param eventDate Время и дата из объекта события
     */
    private void checkEventDate(LocalDateTime eventDate) {
        LocalDateTime timeNow = LocalDateTime.now().plusHours(1L);
        if (eventDate != null && eventDate.isBefore(timeNow)) {
            throw new BadRequestException("Событие должно содержать дату, которая еще не наступила. " +
                    "Текущее значение: " + eventDate);
        }
    }

    /**
     * Метод определения статуса события
     *
     * @param stateAction Текущий статус из объекта события
     * @return Новый статус после определения
     */
    private EventState determiningTheStatusForEvent(ActionState stateAction) {
        return getEventStateDeterming(stateAction);
    }

    /**
     * Метод проверки доступности события
     *
     * @param event Объект события
     */
    private void eventAvailability(Event event) {
        if (event.getState().equals(EventState.PUBLISHED) || event.getState().equals(EventState.CANCELED)) {
            throw new ForbiddenEventException("Статус события не позволяет редактировать событие, статус: " + event.getState());
        }
    }
}