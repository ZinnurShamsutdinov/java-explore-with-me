package ru.practicum.main.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main.entity.dto.user.NewUserDto;
import ru.practicum.main.entity.dto.user.UserDto;
import ru.practicum.main.entity.dto.user.UserShortDto;
import ru.practicum.main.entity.models.User;

/**
 * Утилитарный класс UserMapper для преобразования User / UserDto / UserShortDto / NewUserRequest
 */
@UtilityClass
public class UserMapper {

    /**
     * Преобразование User в UserDto
     *
     * @param user Объект User
     * @return Преобразованный объект UserDto
     */
    public UserDto userToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    /**
     * Преобразование User в UserShortDto
     *
     * @param user Объект User
     * @return Преобразованный объект UserShortDto
     */
    public UserShortDto userToUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    /**
     * Преобразование NewUserDto в User
     *
     * @param newUserDto Объект NewUserRequest
     * @return Преобразованный объект User
     */
    public User newUserRequestToUser(NewUserDto newUserDto) {
        return User.builder()
                .name(newUserDto.getName())
                .email(newUserDto.getEmail())
                .build();
    }
}