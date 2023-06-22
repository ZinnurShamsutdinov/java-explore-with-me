package ru.practicum.main.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.entity.models.User;

import java.util.List;

/**
 * Интерфейс UserRepository для обработки запросов к БД
 */
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByIdIn(List<Long> ids, Pageable pageable);
}