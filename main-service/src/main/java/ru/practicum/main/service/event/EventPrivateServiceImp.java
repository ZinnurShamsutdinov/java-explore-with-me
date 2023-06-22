package ru.practicum.main.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.entity.dto.EventRequestStatusUpdateRequest;
import ru.practicum.main.entity.dto.EventRequestStatusUpdateResult;
import ru.practicum.main.entity.dto.UpdateEventUserRequest;
import ru.practicum.main.entity.dto.event.EventFullDto;
import ru.practicum.main.entity.dto.event.EventShortDto;
import ru.practicum.main.entity.dto.event.NewEventDto;
import ru.practicum.main.entity.dto.request.ParticipationRequestDto;
import ru.practicum.main.entity.enums.ActionState;
import ru.practicum.main.entity.enums.EventState;
import ru.practicum.main.entity.enums.RequestStatus;
import ru.practicum.main.entity.models.Category;
import ru.practicum.main.entity.models.Event;
import ru.practicum.main.entity.models.Request;
import ru.practicum.main.entity.models.User;
import ru.practicum.main.exception.BadRequestException;
import ru.practicum.main.exception.ConflictRequestException;
import ru.practicum.main.exception.ForbiddenEventException;
import ru.practicum.main.mapper.EventMapper;
import ru.practicum.main.mapper.LocationMapper;
import ru.practicum.main.mapper.RequestMapper;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.service.FindObjectInService;
import ru.practicum.main.repository.RequestRepository;
import ru.practicum.main.utilities.DateFormatter;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Класс EventPrivateServiceImp имплементация интерфейса EventPrivateService для отработки логики запросов и логирования
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EventPrivateServiceImp implements EventPrivateService {

    private final EventRepository eventRepository;
    private final FindObjectInService findObjectInService;
    private final RequestRepository requestRepository;
    private final ProcessingEvents processingEvents;

    /**
     * Имплементация метода получения списка событий добавленных текущим пользователем
     *
     * @param userId ID текущего пользователя
     * @param from   Количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size   Количество элементов в наборе
     * @return Список событий
     */
    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> get(Long userId, int from, int size, HttpServletRequest request) {
        findObjectInService.getUserById(userId);
        Pageable pageable = PageRequest.of(from, size);
        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageable);
        List<Event> eventsAddViews = processingEvents.addViewsInEventsList(events, request);
        List<Event> newEvents = processingEvents.confirmRequests(eventsAddViews);
        log.info("Получен приватный запрос на получение всех событий для пользователя с id: {}", userId);
        return newEvents.stream().map(EventMapper::eventToEventShortDto).collect(Collectors.toList());
    }

    /**
     * Имплементация метода добавления нового события
     *
     * @param userId      ID текущего пользователя
     * @param newEventDto Объект NewEventDto
     * @return Объект добавленного события EventFullDto
     */
    @Override
    @Transactional
    public EventFullDto create(Long userId, NewEventDto newEventDto) {
        NewEventDto tempNewEventDto = NewEventDto.builder()
                .annotation(newEventDto.getAnnotation())
                .category(newEventDto.getCategory())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .location(newEventDto.getLocation())
                .paid(newEventDto.getPaid() != null && newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit() == null ? 0 : newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration() == null || newEventDto.getRequestModeration())
                .title(newEventDto.getTitle())
                .build();
        checkEventDate(DateFormatter.formatDate(tempNewEventDto.getEventDate()));
        User user = findObjectInService.getUserById(userId);
        Category category = findObjectInService.getCategoryById(tempNewEventDto.getCategory());
        Long views = 0L;
        Long confirmedRequests = 0L;
        Event event = EventMapper.newEventDtoToCreateEvent(tempNewEventDto, user, category, views, confirmedRequests);
        log.info("Получен приватный запрос на добавление события пользователем с id: {}", userId);
        return EventMapper.eventToEventFullDto(eventRepository.save(event));
    }

    /**
     * Имплементация метода получения полной информации о событии добавленном текущим пользователем
     *
     * @param userId  ID текущего пользователя
     * @param eventId ID события
     * @return Искомый объект EventFullDto
     */
    @Override
    @Transactional(readOnly = true)
    public EventFullDto get(Long userId, Long eventId, HttpServletRequest request) {
        User user = findObjectInService.getUserById(userId);
        Event event = findObjectInService.getEventById(eventId);
        checkOwnerEvent(event, user);
        addEventConfirmRequestAndSetViews(event, request);
        log.info("Получен приватный запрос на получение события с id: {} для пользователя с id: {}", eventId, userId);
        return EventMapper.eventToEventFullDto(event);
    }

    /**
     * Имплементация метода изменения события добавленного текущим пользователем
     *
     * @param userId      ID текущего пользователя
     * @param eventId     ID события
     * @param updateEvent содержит данные объекта UpdateEventUserRequest
     * @return Изменённый объект события EventFullDto
     */
    @Override
    @Transactional
    public EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest updateEvent, HttpServletRequest request) {
        if (updateEvent.getEventDate() != null) {
            checkEventDate(DateFormatter.formatDate(updateEvent.getEventDate()));
        }
        Event event = findObjectInService.getEventById(eventId);
        User user = findObjectInService.getUserById(userId);
        checkOwnerEvent(event, user);
        eventAvailability(event);
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
            event.setState(determiningTheStatusForEvent(updateEvent.getStateAction()));
        }
        if (updateEvent.getTitle() != null && !updateEvent.getTitle().isBlank()) {
            event.setTitle(updateEvent.getTitle());
        }
        if (event.getState().equals(EventState.PUBLISHED)) {
            addEventConfirmRequestAndSetViews(event, request);
        } else {
            event.setViews(0L);
            event.setConfirmedRequests(0L);
        }
        log.info("Получен приватный запрос на обновление события с id: {} для пользователя с id: {}", eventId, userId);
        return EventMapper.eventToEventFullDto(eventRepository.save(event));
    }

    /**
     * Имплементация метода получения списка запросов на участие в событии текущего пользователя
     *
     * @param userId  ID текущего пользователя
     * @param eventId ID события
     * @return Список заявок на участие
     */
    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequests(Long userId, Long eventId, HttpServletRequest request) {
        try {
            Event event = findObjectInService.getEventById(eventId);
            User user = findObjectInService.getUserById(userId);
            checkOwnerEvent(event, user);
            List<Request> requests = requestRepository.findAllByEvent(event);
            log.info("Получен приватный запрос на получение всех запросов для события с id: {} для пользователя с id: {}", eventId, userId);
            return requests.stream().map(RequestMapper::requestToParticipationRequestDto).collect(Collectors.toList());
        } catch (Exception e) {
            throw new BadRequestException("Некорректный запрос получения списка запросов на участие в текущем событии");
        }
    }

    /**
     * Имплементация метода изменения статуса (подтверждена/отменена) заявок на участие в событии текущего пользователя
     *
     * @param userId       ID текущего пользователя
     * @param eventId      ID события
     * @param eventRequest Изменение статуса запроса на участие в событии текущего пользователя
     * @return Результат подтверждения/отклонения заявок на участие в событии
     */
    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateStatus(Long userId, Long eventId,
                                                       EventRequestStatusUpdateRequest eventRequest,
                                                       HttpServletRequest request) {
        Event event = findObjectInService.getEventById(eventId);
        User user = findObjectInService.getUserById(userId);
        checkOwnerEvent(event, user);
        log.info("Получен приватный запрос на обновление статуса запроса для событие с id: {} для пользователя с id: {}", eventId, userId);
        if (event.getState().equals(EventState.PUBLISHED)) {
            addEventConfirmRequestAndSetViews(event, request);
        } else {
            event.setViews(0L);
            event.setConfirmedRequests(0L);
        }
        if (event.getParticipantLimit() <= event.getConfirmedRequests()) {
            log.warn("Достигнут лимит по заявкам на данное событие с id: {}", eventId);
            throw new ForbiddenEventException("Достигнут лимит по заявкам на данное событие с id: " + eventId);
        }
        List<Request> requests = requestRepository.findAllByIdIsIn(eventRequest.getRequestIds());
        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            return new EventRequestStatusUpdateResult(new ArrayList<>(), new ArrayList<>());
        } else if (eventRequest.getStatus().equals(RequestStatus.CONFIRMED)) {
            List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
            List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
            long count = processingEvents.confirmedRequestsForOneEvent(event, RequestStatus.CONFIRMED);
            event.setConfirmedRequests(count);
            for (Request requestList : requests) {
                if (!requestList.getStatus().equals(RequestStatus.PENDING)) {
                    throw new ConflictRequestException("Статус заявки " + requestList.getId()
                            + " не получается ее одобрить, текущий статус " + requestList.getStatus());
                }
                if (event.getConfirmedRequests() <= event.getParticipantLimit()) {
                    requestList.setStatus(RequestStatus.CONFIRMED);
                    confirmedRequests.add(RequestMapper.requestToParticipationRequestDto(requestList));
                    requestRepository.save(requestList);
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1L);
                } else {
                    requestList.setStatus(RequestStatus.REJECTED);
                    rejectedRequests.add(RequestMapper.requestToParticipationRequestDto(requestList));
                    requestRepository.save(requestList);
                }
            }
            eventRepository.save(event);
            return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
        } else if (eventRequest.getStatus().equals(RequestStatus.REJECTED)) {
            EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult(new ArrayList<>(), new ArrayList<>());
            List<ParticipationRequestDto> rejectedRequests = addRejectedRequests(requests);
            result.getRejectedRequests().addAll(rejectedRequests);
            return result;
        }
        return new EventRequestStatusUpdateResult(new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Метод проверки времени и даты от текущего времени
     *
     * @param eventDate Время и дата из объекта события
     */
    private void checkEventDate(LocalDateTime eventDate) {
        if (eventDate != null) {
            LocalDateTime timeNow = LocalDateTime.now().plusHours(2L);
            if (eventDate.isBefore(timeNow)) {
                throw new BadRequestException("Событие должно содержать дату, которая еще не наступила. " +
                        "Текущее значение: " + eventDate);
            }
        }
    }

    /**
     * Метод проверки пользователя на участие в своём событии
     *
     * @param user  Объект пользователя
     * @param event Объект события
     */
    private void checkOwnerEvent(Event event, User user) {
        if (!Objects.equals(event.getInitiator().getId(), user.getId())) {
            throw new ForbiddenEventException("Событие с id:" + event.getId() + " не принадлежит пользователю с id:" + user.getId());
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

    static EventState getEventStateDeterming(ActionState stateAction) {
        if (stateAction.equals(ActionState.SEND_TO_REVIEW)) {
            return EventState.PENDING;
        } else if (stateAction.equals(ActionState.CANCEL_REVIEW)) {
            return EventState.CANCELED;
        } else if (stateAction.equals(ActionState.PUBLISH_EVENT)) {
            return EventState.PUBLISHED;
        } else if (stateAction.equals(ActionState.REJECT_EVENT)) {
            return EventState.CANCELED;
        } else {
            throw new BadRequestException("Статус не соответствует модификатору доступа");
        }
    }

    /**
     * Метод проверки доступности события
     *
     * @param event Объект события
     */
    private void eventAvailability(Event event) {
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ForbiddenEventException("Статус события не позволяет редактировать событие, статус: " + event.getState());
        }
    }

    /**
     * Метод добавления статуса запросов
     *
     * @param requests Список запросов
     * @return Список данных заявок на участие в событии
     */
    private List<ParticipationRequestDto> addRejectedRequests(List<Request> requests) {
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        for (Request requestList : requests) {
            if (!requestList.getStatus().equals(RequestStatus.PENDING)) {
                throw new ConflictRequestException("Статус заявки " + requestList.getId()
                        + " не получается ее одобрить, текущий статус " + requestList.getStatus());
            }
            requestList.setStatus(RequestStatus.REJECTED);
            requestRepository.save(requestList);
            rejectedRequests.add(RequestMapper.requestToParticipationRequestDto(requestList));
        }
        return rejectedRequests;
    }

    /**
     * Метод добавления подтверждённых событий
     *
     * @param event Объект события
     */
    private void addEventConfirmRequestAndSetViews(Event event, HttpServletRequest request) {
        long count = processingEvents.confirmedRequestsForOneEvent(event, RequestStatus.CONFIRMED);
        event.setConfirmedRequests(count);
        long views = processingEvents.searchViews(event, request);
        event.setViews(views);
    }
}