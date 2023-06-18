package ru.practicum.main.service.user;

import ru.practicum.main.entity.dto.user.NewUserDto;
import ru.practicum.main.entity.dto.user.UserDto;

import java.util.List;

//Интерфейс UserAdminService для обработки логики запросов из UserAdminController
public interface UserAdminService {


    // Метод получения информации о пользователях
    List<UserDto> get(List<Long> ids, int from, int size);

    // Метод создания пользователя
    UserDto create(NewUserDto newUserDto);


    //Метод удаления пользователя по ID
    void delete(Long id);
}