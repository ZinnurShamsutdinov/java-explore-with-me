package ru.practicum.main.service.comment;

import ru.practicum.main.entity.dto.comment.CommentDto;

import java.util.List;

/**
 * Интерфейс CommentsPublicService для обработки логики запросов из CommentsPublicController
 */
public interface CommentPublicService {

    /**
     * Метод получения списка комментариев по ID события
     *
     * @param id   ID события
     * @param from Количество комментариев, которые нужно пропустить для формирования текущего набора
     * @param size Количество комментариев в наборе
     * @return Список комментариев по событию
     */
    List<CommentDto> get(Long id, Integer from, Integer size);
}