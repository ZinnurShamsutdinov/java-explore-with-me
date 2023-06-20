package ru.practicum.main.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main.entity.dto.event.EventFullDto;
import ru.practicum.main.entity.dto.event.EventShortDto;
import ru.practicum.main.entity.dto.event.NewEventDto;
import ru.practicum.main.entity.enums.EventState;
import ru.practicum.main.entity.models.Category;
import ru.practicum.main.entity.models.Event;
import ru.practicum.main.entity.models.User;
import ru.practicum.main.utilities.DateFormatter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Утилитарный класс EventMapper для преобразования Event / EventShortDto / EventFullDto / EventState
 */
@UtilityClass
public class EventMapper {

    /**
     * Преобразование Event в EventShortDto
     *
     * @param event Объект Event
     * @return Преобразованный объект EventShortDto
     */
    public EventShortDto eventToeventShortDto(Event event) {
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.categoryToCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.userToUserShortDto(event.getInitiator()))
                .paid(event.isPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    /**
     * Преобразование Event в EventFullDto
     *
     * @param event Объект Event
     * @return Преобразованный объект EventFullDto
     */
    public EventFullDto eventToEventFullDto(Event event) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.categoryToCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.userToUserShortDto(event.getInitiator()))
                .location(LocationMapper.locationToLocationDto(event.getLocation()))
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.isRequestModeration())
                .state(event.getState().name())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    /**
     * Преобразование нескольких параметров в Event
     *
     * @param newEventDto       Объект NewEventDto
     * @param user              Объект User
     * @param category          Объект Category
     * @param views             Количество просмотрев события
     * @param confirmedRequests Количество одобренных заявок на участие в данном событии
     * @return Преобразованный объект Event
     */
    public static Event newEventDtoToCreateEvent(NewEventDto newEventDto, User user, Category category, Long views,
                                                 Long confirmedRequests) {
        LocalDateTime dateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .confirmedRequests(confirmedRequests)
                .createdOn(dateTime)
                .description(newEventDto.getDescription())
                .eventDate(DateFormatter.formatDate(newEventDto.getEventDate()))
                .initiator(user)
                .location(LocationMapper.locationDtoToLocation(newEventDto.getLocation()))
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .publishedOn(null)
                .requestModeration(newEventDto.getRequestModeration())
                .state(EventState.PENDING)
                .title(newEventDto.getTitle())
                .views(views)
                .build();
    }
}