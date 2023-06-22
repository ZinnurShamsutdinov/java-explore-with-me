package ru.practicum.main.entity.dto.comment;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Модель объекта NewComment Data Transfer Object
 * (Новый комментарий к событию)
 */
@Value
@Builder
public class NewCommentDto {
    @NotBlank(message = "Текст комментария не может быть пустым")
    @Size(max = 7000, message = "Максимальное кол-во символов для комментария: 7000")
    String text;
    @NotNull(message = "Поле userId не может быть пустым")
    Long userId;
    @NotNull(message = "Поле eventId АйДи комментируемого события не может быть пустым")
    Long eventId;
}