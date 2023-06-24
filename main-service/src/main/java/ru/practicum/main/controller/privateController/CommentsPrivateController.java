package ru.practicum.main.controller.privateController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.entity.dto.comment.CommentDto;
import ru.practicum.main.entity.dto.comment.NewCommentDto;
import ru.practicum.main.service.comment.CommentPrivateService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Класс CommentsPrivateController по энпоинту private/comments
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/private/comments")
public class CommentsPrivateController {

    private final CommentPrivateService commentPrivateService;

    /**
     * Метод (эндпоинт) получения комментария по ID комментария и ID пользователя
     *
     * @param commentId ID комментария
     * @param userId    ID пользователя
     * @return Объект CommentDto
     */
    @GetMapping("/{commentId}/user/{userId}")
    public CommentDto get(@PathVariable Long commentId,
                          @PathVariable Long userId) {
        return commentPrivateService.get(commentId, userId);
    }

    /**
     * Метод (эндпоинт) получения списка комментариев по ID события и ID пользователя
     *
     * @param eventId ID события
     * @param userId  ID пользователя
     * @param from    Количество комментариев, которые нужно пропустить для формирования текущего набора
     * @param size    Количество комментариев в наборе
     * @return Список комментариев по событию
     */
    @GetMapping("/event/{eventId}/user/{userId}")
    public List<CommentDto> get(@PathVariable Long eventId,
                                @PathVariable Long userId,
                                @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                @Positive @RequestParam(defaultValue = "10") Integer size) {
        return commentPrivateService.get(eventId, userId, from, size);
    }

    /**
     * Метод (эндпоинт) добавления комментария
     *
     * @param newCommentDto Новый комментарий в виде объекта NewCommentDto
     * @return Добавленный комментарий в виде объекта CommentDto
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto create(@Validated @RequestBody NewCommentDto newCommentDto) {
        return commentPrivateService.create(newCommentDto);
    }

    /**
     * Метод (эндпоинт) изменения комментария по ID
     *
     * @param commentId     ID комментария
     * @param newCommentDto Новый изменённый комментарий в виде объекта NewCommentDto
     * @return Изменённый комментарий в виде объекта CommentDto
     */
    @PatchMapping("/{commentId}")
    public CommentDto update(@PathVariable Long commentId,
                             @Validated @RequestBody NewCommentDto newCommentDto) {
        return commentPrivateService.update(commentId, newCommentDto);
    }

    /**
     * Метод (эндпоинт) удаления по ID комментария и ID пользователя
     *
     * @param commentId ID комментария
     * @param userId    ID пользователя
     */
    @DeleteMapping("/{commentId}/user/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long commentId,
                       @PathVariable Long userId) {
        commentPrivateService.delete(commentId, userId);
    }
}