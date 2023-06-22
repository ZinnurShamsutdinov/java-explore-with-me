package ru.practicum.main.service.user;

import ru.practicum.main.entity.dto.user.NewUserDto;
import ru.practicum.main.entity.dto.user.UserDto;

import java.util.List;

/**
 * Интерфейс UserAdminService для обработки логики запросов из UserAdminController
 */
public interface UserAdminService {

    /**
     * Метод получения информации о пользователях
     *
     * @param ids  ID пользователей
     * @param from количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size количество элементов в наборе
     * @return Возвращает список пользователей
     */
    List<UserDto> get(List<Long> ids, int from, int size);

    /**
     * Метод создания пользователя
     *
     * @param newUserDto Объект нового пользователя
     * @return Созданный объект пользователя
     */
    UserDto create(NewUserDto newUserDto);

    /**
     * Метод удаления пользователя по ID
     *
     * @param id идентификатор пользователя
     */
    void delete(Long id);
}