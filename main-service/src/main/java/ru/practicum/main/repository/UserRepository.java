package ru.practicum.main.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.entity.models.User;
import ru.practicum.main.exception.ResourceNotFoundException;

import java.util.List;

/**
 * Интерфейс UserRepository для обработки запросов к БД
 */
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByIdIn(List<Long> ids, Pageable pageable);

    default User get(long id) {
        return findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Пользователь c id: " + id + " не существует"));
    }
}