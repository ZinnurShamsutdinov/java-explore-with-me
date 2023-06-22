package ru.practicum.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.common.dto.ViewStats;
import ru.practicum.stats.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Интерфейс StatisticRepository для обработки запросов к БД
 */
public interface StatisticRepository extends JpaRepository<EndpointHit, Long> {

    /**
     * Метод получения всей статистики с уникального IP
     *
     * @param start Дата и время начала диапазона за который нужно выгрузить статистику
     * @param end   Дата и время конца диапазона за который нужно выгрузить статистику
     * @return Сформированный список статистики по посещениям
     */
    @Query("SELECT new ru.practicum.common.dto.ViewStats(st.app, st.uri, COUNT(DISTINCT st.ip)) " +
            "FROM EndpointHit as st " +
            "WHERE st.timestamp BETWEEN :start AND :end " +
            "GROUP BY st.app, st.uri " +
            "ORDER BY COUNT(DISTINCT st.ip) DESC")
    List<ViewStats> getAllStatisticByDistinctIp(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    /**
     * Метод получения всей статистики
     *
     * @param start Дата и время начала диапазона за который нужно выгрузить статистику
     * @param end   Дата и время конца диапазона за который нужно выгрузить статистику
     * @return Сформированный список статистики по посещениям
     */
    @Query("SELECT new ru.practicum.common.dto.ViewStats(st.app, st.uri, COUNT(st.ip)) " +
            "FROM EndpointHit as st " +
            "WHERE st.timestamp BETWEEN :start AND :end " +
            "GROUP BY st.app, st.uri " +
            "ORDER BY COUNT(st.ip) DESC")
    List<ViewStats> getAllStatistic(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    /**
     * Метод получения списка посещений по времени начала и конца только с уникальным ip
     *
     * @param start Дата и время начала диапазона за который нужно выгрузить статистику
     * @param end   Дата и время конца диапазона за который нужно выгрузить статистику
     * @param uri  Список uri для которых нужно выгрузить статистику
     * @return Сформированный список статистики по посещениям
     */
    @Query("SELECT new ru.practicum.common.dto.ViewStats(st.app, st.uri, COUNT(DISTINCT st.ip)) " +
            "FROM EndpointHit as st " +
            "WHERE st.uri IN (:uri) " +
            "AND st.timestamp BETWEEN :start AND :end " +
            "GROUP BY st.app, st.uri " +
            "ORDER BY COUNT(DISTINCT st.ip) DESC")
    List<ViewStats> getStatisticByUrisDistinctIps(
            @Param("uri") List<String> uri,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    /**
     * Метод получения списка посещений по времени начала и конца
     *
     * @param start Дата и время начала диапазона за который нужно выгрузить статистику
     * @param end   Дата и время конца диапазона за который нужно выгрузить статистику
     * @param uri  Список uri для которых нужно выгрузить статистику
     * @return Сформированный список статистики по посещениям
     */
    @Query("SELECT NEW ru.practicum.common.dto.ViewStats(st.app, st.uri, COUNT(st.ip)) " +
            "FROM EndpointHit as st " +
            "WHERE st.uri IN(:uri) " +
            "AND st.timestamp BETWEEN :start AND :end " +
            "GROUP BY st.app, st.uri " +
            "ORDER BY COUNT(st.ip) DESC")
    List<ViewStats> getStatisticByUris(
            @Param("uri") List<String> uri,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}