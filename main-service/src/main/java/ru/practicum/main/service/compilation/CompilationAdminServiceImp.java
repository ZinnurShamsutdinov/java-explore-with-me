package ru.practicum.main.service.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.entity.dto.UpdateCompilationRequest;
import ru.practicum.main.entity.dto.compilation.CompilationDto;
import ru.practicum.main.entity.dto.compilation.NewCompilationDto;
import ru.practicum.main.entity.models.Compilation;
import ru.practicum.main.entity.models.Event;
import ru.practicum.main.mapper.CompilationMapper;
import ru.practicum.main.repository.CompilationRepository;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.service.FindObjectInService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

//Класс CompilationAdminServiceImp для отработки логики запросов и логирования
@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationAdminServiceImp implements CompilationAdminService {

    private final CompilationRepository compilationRepository;
    private final FindObjectInService findObjectInService;
    private final EventRepository eventRepository;

    /**
     * Имплементация метода создания подборки
     *
     * @param newCompilationDto Объект NewCompilationDto
     * @return Созданный объект подборки NewCompilationDto
     */
    @Override
    @Transactional
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        Set<Event> events = new HashSet<>();
        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            events = addEvents(newCompilationDto.getEvents());
        }
        Compilation compilation = CompilationMapper.newCompilationDtoToCompilationAndEvents(newCompilationDto, events);
        log.info("Получен запрос на добавление подборки событий: {}", newCompilationDto.getTitle());
        return CompilationMapper.compilationToCompilationDto(compilationRepository.save(compilation));
    }

    /**
     * Имплементация метода обновления подборки по ID
     *
     * @param compId                   ID подборки
     * @param updateCompilationRequest Объект подборки UpdateCompilationRequest
     * @return Изменённый объект подборки CompilationDto
     */
    @Override
    @Transactional
    public CompilationDto update(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation newCompilation = findObjectInService.getCompilationById(compId);
        Set<Event> events;
        if (updateCompilationRequest.getEvents() != null) {
            events = addEvents(updateCompilationRequest.getEvents());
            newCompilation.setEvents(events);
        }
        if (updateCompilationRequest.getPinned() != null) {
            newCompilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (updateCompilationRequest.getTitle() != null && updateCompilationRequest.getTitle().isBlank()) {
            newCompilation.setTitle(updateCompilationRequest.getTitle());
        }
        log.info("Получен запрос на обновление подборки событий по id: {}", compId);
        return CompilationMapper.compilationToCompilationDto(compilationRepository.save(newCompilation));
    }

    /**
     * Имплементация метода удаления подборки по ID
     *
     * @param compId ID подборки
     */
    @Override
    @Transactional
    public void delete(Long compId) {
        findObjectInService.getCompilationById(compId);
        log.info("Получен запрос на удаление подборки событий по id: {}", compId);
        compilationRepository.deleteById(compId);
    }

    /**
     * Метод поиска и добавления событий в подборку
     *
     * @param eventsIds Список ID событий
     * @return Список событий
     */
    private Set<Event> addEvents(List<Long> eventsIds) {
        return eventRepository.findAllByIdIsIn(eventsIds);
    }
}