package ru.practicum.main.service.request;

import ru.practicum.main.entity.dto.request.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


//Интерфейс RequestPrivateService для обработки логики запросов из RequestPrivateController
public interface RequestPrivateService {

    //Метод получения информации о заявках текущего пользователя на участие в чужих событиях
    List<ParticipationRequestDto> get(Long id);

    //Метод добавление запроса от текущего пользователя на участие в событии
    ParticipationRequestDto create(Long userId, Long eventId, HttpServletRequest request);


    //Метод отмены своего запроса на участие в событии
    ParticipationRequestDto update(Long userId, Long requestId, HttpServletRequest request);
}