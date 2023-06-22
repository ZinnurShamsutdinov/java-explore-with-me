package ru.practicum.main.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.main.entity.dto.user.NewUserDto;
import ru.practicum.main.entity.dto.user.UserDto;
import ru.practicum.main.entity.models.User;
import ru.practicum.main.exception.BadRequestException;
import ru.practicum.main.exception.ConflictNameAndEmailException;
import ru.practicum.main.mapper.UserMapper;
import ru.practicum.main.service.FindObjectInService;
import ru.practicum.main.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс UserAdminServiceImp для отработки логики запросов и логирования имплементирует классы интерфейса UserAdminService
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserAdminServiceImp implements UserAdminService {

    private final UserRepository userRepository;
    private final FindObjectInService findObjectInService;

    /**
     * Метод получения информации о пользователях
     *
     * @param ids  ID пользователей
     * @param from количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size количество элементов в наборе
     * @return Возвращает список пользователей
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserDto> get(List<Long> ids, int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        if (ids == null) {
            log.info("Получен запрос на получение списка пользователей без id");
            return userRepository.findAll(pageable).stream()
                    .map(UserMapper::userToDto)
                    .collect(Collectors.toList());
        } else {
            log.info("Получен запрос на получение списка пользователей по id");
            return userRepository.findByIdIn(ids, pageable).stream()
                    .map(UserMapper::userToDto)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Метод создания пользователя
     *
     * @param newUserDto Объект нового пользователя
     * @return Созданный объект пользователя
     */
    @Override
    @Transactional
    public UserDto create(NewUserDto newUserDto) {
        User user = UserMapper.newUserRequestToUser(newUserDto);
        try {
            log.info("Получен запрос на добавление пользователя {}", newUserDto);
            return UserMapper.userToDto(userRepository.save(user));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictNameAndEmailException("E-mail: " + newUserDto.getEmail() + " или name пользователя: " +
                    newUserDto.getName() + " уже есть в базе");
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Запрос на добавление пользователя" + newUserDto + " составлен неправильно");
        }
    }

    /**
     * Метод удаления пользователя по ID
     *
     * @param id идентификатор пользователя
     */
    @Override
    @Transactional
    public void delete(Long id) {
        User user = findObjectInService.getUserById(id);
        log.info("Получен запрос на удаление пользователя с id: {}", id);
        userRepository.delete(user);
    }
}