package ru.practicum.main.service.comment;

import ru.practicum.main.entity.dto.comment.CommentDto;
import ru.practicum.main.entity.dto.comment.NewCommentDto;

import java.util.List;

/**
 * Интерфейс CommentsPrivateService для обработки логики запросов из CommentsPrivateController
 */
public interface CommentPrivateService {

    /**
     * Метод получения комментария по ID комментария и ID пользователя
     *
     * @param commentId ID комментария
     * @param userId    ID пользователя
     * @return Объект CommentDto
     */
    CommentDto get(Long commentId, Long userId);

    /**
     * Метод получения списка комментариев по ID события и ID пользователя
     *
     * @param eventId ID события
     * @param userId  ID пользователя
     * @param from    Количество комментариев, которые нужно пропустить для формирования текущего набора
     * @param size    Количество комментариев в наборе
     * @return Список комментариев по событию
     */
    List<CommentDto> get(Long eventId, Long userId, Integer from, Integer size);

    /**
     * Метод добавления комментария
     *
     * @param newCommentDto Новый комментарий в виде объекта InputCommentDto
     * @return Добавленный комментарий в виде объекта CommentDto
     */
    CommentDto create(NewCommentDto newCommentDto);

    /**
     * Метод изменения комментария по ID
     *
     * @param commentId     ID комментария
     * @param newCommentDto Новый изменённый комментарий в виде объекта InputCommentDto
     * @return Изменённый комментарий в виде объекта CommentDto
     */
    CommentDto update(Long commentId, NewCommentDto newCommentDto);

    /**
     * Метод удаления по ID комментария и ID пользователя
     *
     * @param commentId ID комментария
     * @param userId    ID пользователя
     */
    void delete(Long commentId, Long userId);

}