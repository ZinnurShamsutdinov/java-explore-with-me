package ru.practicum.main.controller.publicController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.entity.dto.comment.CommentDto;
import ru.practicum.main.service.comment.CommentPublicService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Класс CommentsPublicController по энпоинту comments/event/{eventId}
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/comments/event/{eventId}")
public class CommentsPublicController {

    private final CommentPublicService commentPublicService;

    /**
     * Метод (эндпоинт) получения списка комментариев по ID события
     *
     * @param eventId ID события
     * @param from    Количество комментариев, которые нужно пропустить для формирования текущего набора
     * @param size    Количество комментариев в наборе
     * @return Список комментариев по событию
     */
    @GetMapping()
    public List<CommentDto> get(@PathVariable Long eventId,
                                @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                @Positive @RequestParam(defaultValue = "10") Integer size) {
        return commentPublicService.get(eventId, from, size);
    }
}