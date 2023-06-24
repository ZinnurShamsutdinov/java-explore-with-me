package ru.practicum.main.service.comment;

import ru.practicum.main.entity.dto.comment.CommentDto;
import ru.practicum.main.entity.dto.comment.NewCommentDto;
import ru.practicum.main.entity.dto.comment.UpdateCommentDto;

import java.util.List;

/**
 * Интерфейс CommentsAdminService для обработки логики запросов из CommentsAdminController
 */
public interface CommentAdminService {

    /**
     * Метод получения комментария по ID
     *
     * @param id ID комментария
     * @return Объект CommentDto
     */
    CommentDto get(Long id);

    /**
     * Метод получения списка комментариев по ID события
     *
     * @param id   ID события
     * @param from Количество комментариев, которые нужно пропустить для формирования текущего набора
     * @param size Количество комментариев в наборе
     * @return Список комментариев по событию
     */
    List<CommentDto> get(Long id, Integer from, Integer size);

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
     * @param id            ID комментария
     * @param updateComment Новый изменённый комментарий в виде объекта UpdateCommentAdminDto
     * @return Изменённый комментарий в виде объекта CommentDto
     */
    CommentDto update(Long id, UpdateCommentDto updateComment);

    /**
     * Метод удаления комментария по ID
     *
     * @param id ID комментария
     */
    void delete(Long id);
}