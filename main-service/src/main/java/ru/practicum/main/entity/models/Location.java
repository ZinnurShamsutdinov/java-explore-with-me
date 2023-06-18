package ru.practicum.main.entity.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

//Модель сущности Location
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class Location {
    private float lat;
    private float lon;
}