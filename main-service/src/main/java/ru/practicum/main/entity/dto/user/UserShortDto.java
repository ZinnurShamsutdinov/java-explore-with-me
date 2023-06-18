package ru.practicum.main.entity.dto.user;

import lombok.Builder;
import lombok.Value;

//Модель объекта UserShort Data Transfer Object
@Value
@Builder
public class UserShortDto {
    Long id;
    String name;
}