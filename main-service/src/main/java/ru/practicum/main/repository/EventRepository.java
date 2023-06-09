package ru.practicum.main.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.main.entity.enums.EventState;
import ru.practicum.main.entity.models.Category;
import ru.practicum.main.entity.models.Event;
import ru.practicum.main.exception.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Интерфейс EventRepository для обработки запросов к БД
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findEventByCategoryIs(Category category);

    Set<Event> findAllByIdIsIn(List<Long> id);

    List<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    @Query(value = "SELECT * FROM events " +
            "WHERE (initiator_id IN :users OR :users IS NULL) " +
            "AND state IN :states " +
            "AND (category_id IN :categories  OR :categories IS NULL) " +
            "AND (event_date >= to_timestamp(:rangeStart, 'yyyy-mm-dd hh24:mi:ss')  " +
            "OR to_timestamp(:rangeStart, 'yyyy-mm-dd hh24:mi:ss') IS NULL) " +
            "AND (event_date <= to_timestamp(:rangeEnd, 'yyyy-mm-dd hh24:mi:ss')   " +
            "OR to_timestamp(:rangeEnd, 'yyyy-mm-dd hh24:mi:ss') IS NULL) " +
            "OFFSET :from LIMIT :size", nativeQuery = true)
    List<Event> findAllByAdmin(@Param("users") List<Long> users,
                               @Param("states") List<String> states,
                               @Param("categories") List<Long> categories,
                               @Param("rangeStart") LocalDateTime rangeStart,
                               @Param("rangeEnd") LocalDateTime rangeEnd,
                               @Param("from") Integer from,
                               @Param("size") Integer size);

    @Query("SELECT e FROM Event e " +
            "WHERE (:users IS NULL OR e.initiator.id IN :users) " +
            "AND (:states IS NULL OR e.state IN :states) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (cast(:rangeStart AS java.time.LocalDateTime) IS NULL OR e.eventDate >= :rangeStart) " +
            "AND (cast(:rangeEnd AS java.time.LocalDateTime) IS NULL OR e.eventDate <= :rangeEnd)")
    List<Event> findAllByAdminAndState(@Param("users") List<Long> users,
                                       @Param("states") List<EventState> states,
                                       @Param("categories") List<Long> categories,
                                       @Param("rangeStart") LocalDateTime rangeStart,
                                       @Param("rangeEnd") LocalDateTime rangeEnd,
                                       Pageable pageable);

    @Query(value = "SELECT * " +
            "FROM events  " +
            "WHERE (lower(annotation) LIKE '%'||lower(:text)||'%' OR lower(description) LIKE '%'||lower(:text)||'%') " +
            "AND (category_id IN :categories  OR :categories IS NULL) " +
            "AND (:paid IS NULL OR paid = :paid) " +
            "AND (event_date BETWEEN " +
            "to_timestamp(:rangeStart, 'yyyy-mm-dd hh24:mi:ss') AND to_timestamp(:rangeEnd, 'yyyy-mm-dd hh24:mi:ss') " +
            "OR event_date > CURRENT_TIMESTAMP) " +
            "ORDER BY lower(:sort) " +
            "OFFSET :from " +
            "LIMIT :size", nativeQuery = true)
    List<Event> findAllByPublic(@Param("text") String text,
                                @Param("categories") List<Long> categories,
                                @Param("paid") Boolean paid,
                                @Param("rangeStart") String rangeStart,
                                @Param("rangeEnd") String rangeEnd,
                                @Param("sort") String sort,
                                @Param("from") Integer from,
                                @Param("size") Integer size);

    Optional<Event> findEventByIdAndStateIs(Long id, EventState state);


    default Event get(long id) {
        return findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Событие c id:  " + id + " не существует"));
    }

}