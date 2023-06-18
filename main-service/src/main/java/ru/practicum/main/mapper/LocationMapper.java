package ru.practicum.main.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main.entity.dto.location.LocationDto;
import ru.practicum.main.entity.models.Location;

//Утилитарный класс LocationMapper для преобразования Location / LocationDto
@UtilityClass
public class LocationMapper {

    //Преобразование LocationDto в Location
    public Location locationDtoToLocation(LocationDto locationDto) {
        return Location.builder()
                .lat(locationDto.getLat())
                .lon(locationDto.getLon())
                .build();
    }

    //Преобразование Location в LocationDto
    public LocationDto locationToLocationDto(Location location) {
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }
}