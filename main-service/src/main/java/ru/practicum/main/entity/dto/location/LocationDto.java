package ru.practicum.main.entity.dto.location;

import lombok.Builder;
import lombok.Value;

//Модель объекта Location Data Transfer Object
@Value
@Builder
public class LocationDto {
    float lat;
    float lon;
}