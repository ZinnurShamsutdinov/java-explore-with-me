package ru.practicum.main.entity.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;
import ru.practicum.main.entity.dto.user.UserShortDto;

import java.time.LocalDateTime;

/**
 * Модель объекта Comment Data Transfer Object
 * (Комментарий)
 */
@Value
@Builder
public class CommentDto {
    Long id;
    String text;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt;
    UserShortDto commenter;
    EventCommentDto event;
    String state;
}