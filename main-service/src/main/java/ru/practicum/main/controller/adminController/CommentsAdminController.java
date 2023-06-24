package ru.practicum.main.controller.adminController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.entity.dto.comment.CommentDto;
import ru.practicum.main.entity.dto.comment.NewCommentDto;
import ru.practicum.main.entity.dto.comment.UpdateCommentDto;
import ru.practicum.main.service.comment.CommentAdminService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Класс CommentsAdminController по энпоинту admin/comments
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/comments")
public class CommentsAdminController {

    private final CommentAdminService commentAdminService;

    /**
     * Метод (эндпоинт) получения комментария по ID
     *
     * @param commentId ID комментария
     * @return Объект CommentDto
     */
    @GetMapping("/{commentId}")
    public CommentDto get(@PathVariable Long commentId) {
        return commentAdminService.get(commentId);
    }

    /**
     * Метод (эндпоинт) получения списка комментариев по ID события
     *
     * @param eventId ID события
     * @param from    Количество комментариев, которые нужно пропустить для формирования текущего набора
     * @param size    Количество комментариев в наборе
     * @return Список комментариев по событию
     */
    @GetMapping("/event/{eventId}")
    public List<CommentDto> get(@PathVariable Long eventId,
                                @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                @Positive @RequestParam(defaultValue = "10") Integer size) {
        return commentAdminService.get(eventId, from, size);
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
        return commentAdminService.create(newCommentDto);
    }

    /**
     * Метод (эндпоинт) изменения комментария по ID
     *
     * @param commentId     ID комментария
     * @param updateComment Новый изменённый комментарий в виде объекта UpdateCommentDto
     * @return Изменённый комментарий в виде объекта CommentDto
     */
    @PatchMapping("/{commentId}")
    public CommentDto update(@PathVariable Long commentId,
                             @Validated @RequestBody UpdateCommentDto updateComment) {
        return commentAdminService.update(commentId, updateComment);
    }

    /**
     * Метод (эндпоинт) удаления комментария по ID
     *
     * @param commentId ID комментария
     */
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long commentId) {
        commentAdminService.delete(commentId);
    }
}