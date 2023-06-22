package ru.practicum.stats.service;

import ru.practicum.common.dto.EndpointHitDto;
import ru.practicum.common.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Интерфейс StatisticService для обработки логики запросов из StatisticController
 */
public interface StatisticService {

    /**
     * Метод создания запроса Hit
     *
     * @param endpointHitDto Объект запроса hit
     */
    EndpointHitDto createHit(EndpointHitDto endpointHitDto);

    /**
     * Метод получения статистики по посещениям
     *
     * @param start  Дата и время начала диапазона за который нужно выгрузить статистику
     * @param end    Дата и время конца диапазона за который нужно выгрузить статистику
     * @param uris   Список uri для которых нужно выгрузить статистику
     * @param unique Нужно ли учитывать только уникальные посещения (только с уникальным ip)
     * @return Сформированный список статистики по посещениям
     */
    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}