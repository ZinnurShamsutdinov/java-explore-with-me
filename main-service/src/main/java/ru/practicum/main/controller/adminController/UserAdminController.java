package ru.practicum.main.controller.adminController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.entity.dto.user.NewUserDto;
import ru.practicum.main.entity.dto.user.UserDto;
import ru.practicum.main.service.user.UserAdminService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

//Класс администрирования пользователей UserAdminController по энпоинту admin/users
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class UserAdminController {
    private final UserAdminService userAdminService;

    //Эндпоинт получения информации о пользователях
    @GetMapping
    List<UserDto> get(@RequestParam(required = false) List<Long> ids,
                      @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                      @Positive @RequestParam(defaultValue = "10") Integer size) {
        return userAdminService.get(ids, from, size);
    }

    //Эндпоинт создания пользователя
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserDto create(@Validated @RequestBody NewUserDto newUserDto) {
        return userAdminService.create(newUserDto);
    }

    //Эндпоинт удаления пользователя по ID
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable Long userId) {
        userAdminService.delete(userId);
    }
}