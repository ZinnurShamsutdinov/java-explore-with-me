package ru.practicum.main.entity.dto.user;

import lombok.Builder;
import lombok.Value;

/**
 * Модель объекта User Data Transfer Object
 * (Пользователь)
 */
@Value
@Builder
public class UserDto {
    String email;
    Long id;
    String name;
}
