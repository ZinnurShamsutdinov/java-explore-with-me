package ru.practicum.main.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main.entity.dto.request.ParticipationRequestDto;
import ru.practicum.main.entity.models.Request;

//Утилитарный класс RequestMapper для преобразования Request / ParticipationRequestDto
@UtilityClass
public class RequestMapper {

    //Преобразование Request в ParticipationRequestDto
    public ParticipationRequestDto requestToParticipationRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .id(request.getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus().name())
                .build();
    }
}