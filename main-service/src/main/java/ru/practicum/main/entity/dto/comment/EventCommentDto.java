package ru.practicum.main.entity.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;
import ru.practicum.main.entity.dto.category.CategoryDto;
import ru.practicum.main.entity.dto.user.UserShortDto;

import java.time.LocalDateTime;

/**
 * Модель объекта EventComment Data Transfer Object
 * (Комментарий к событию)
 */
@Value
@Builder
public class EventCommentDto {
    String annotation;
    CategoryDto category;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    Long id;
    UserShortDto initiator;
    String title;
}