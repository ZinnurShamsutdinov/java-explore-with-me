package ru.practicum.main.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.entity.models.*;
import ru.practicum.main.exception.ResourceNotFoundException;

import java.util.List;

//Класс поиска объекта в БД
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindObjectInRepository {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CompilationRepository compilationRepository;
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;

    //Метод проверки категории в базе по ID
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Категория c id: " + id + " не найдена"));
    }

    //Метод проверки пользователя в базе по ID
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Пользователь c id: " + id + " не найден"));
    }

    //Метод проверки подборки события в базе по ID
    public Compilation getCompilationById(Long id) {
        return compilationRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Подборка событий c id = " + id + " не найдена"));
    }

    //Метод проверки события в базе по ID
    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Событие c id = " + id + " не найдено"));
    }

    //Метод проверки запроса в базе по ID
    public Request getRequestById(Long id) {
        return requestRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Запрос на участие c id = " + id + " не найден"));
    }

    //Метод проверки Категории в базе по объекту
    public boolean isRelatedEvent(Category category) {
        List<Event> findEventByCategory = eventRepository.findEventByCategoryIs(category);
        return !findEventByCategory.isEmpty();
    }
}