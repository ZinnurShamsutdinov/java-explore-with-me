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
import ru.practicum.main.repository.FindObjectInRepository;
import ru.practicum.main.repository.UserRepository;


import java.util.List;
import java.util.stream.Collectors;



@Slf4j
@Service
@RequiredArgsConstructor
public class UserAdminServiceImp implements UserAdminService {

    private final UserRepository userRepository;
    private final FindObjectInRepository findObjectInRepository;

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

    @Override
    @Transactional
    public void delete(Long id) {
        User user = findObjectInRepository.getUserById(id);
        log.info("Получен запрос на удаление пользователя с id: {}", id);
        userRepository.delete(user);
    }
}