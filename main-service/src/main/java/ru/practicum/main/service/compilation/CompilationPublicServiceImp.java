package ru.practicum.main.service.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.entity.dto.compilation.CompilationDto;
import ru.practicum.main.mapper.CompilationMapper;
import ru.practicum.main.repository.CompilationRepository;
import ru.practicum.main.service.FindObjectInService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс CompilationPublicServiceImp для отработки логики запросов и логирования
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationPublicServiceImp implements CompilationPublicService {

    private final CompilationRepository compilationRepository;
    private final FindObjectInService findObjectInService;

    /**
     * Имплементация метода получения списка подборок событий
     *
     * @param pinned Закреплена ли подборка на главной странице сайта
     * @param from   Количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size   Количество элементов в наборе
     * @return Список подборок событий
     */
    @Override
    public List<CompilationDto> get(Boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        log.info("Получен запрос на поиск всех подборок событий");
        return compilationRepository.findAllByPinnedIs(pinned, pageable).stream()
                .map(CompilationMapper::compilationToCompilationDto).collect(Collectors.toList());
    }

    /**
     * Имплементация метода получения подборки событий по ID
     *
     * @param id ID подборки событий
     * @return Искомая подборка событий
     */
    @Override
    public CompilationDto get(Long id) {
        log.info("Получен запрос на поиск подборки событий по id: {}", id);
        return CompilationMapper.compilationToCompilationDto(findObjectInService.getCompilationById(id));
    }
}