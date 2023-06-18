package ru.practicum.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.entity.models.Category;

//Интерфейс CategoryRepository для обработки запросов к БД
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Boolean existsByName(String name);
}