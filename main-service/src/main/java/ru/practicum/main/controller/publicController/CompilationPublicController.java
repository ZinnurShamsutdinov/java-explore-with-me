package ru.practicum.main.controller.publicController;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.entity.dto.compilation.CompilationDto;
import ru.practicum.main.service.compilation.CompilationPublicService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

//Класс (Публичный контроллер для работы с подборками событий) CompilationPublicController по энпоинту compilations
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
public class CompilationPublicController {

    private final CompilationPublicService compilationPublicService;

    //Эндпоинт получения списка подборок событий
    @GetMapping()
    public List<CompilationDto> get(@RequestParam(required = false) Boolean pinned,
                                    @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                    @Positive @RequestParam(defaultValue = "10") Integer size) {
        return compilationPublicService.get(pinned, from, size);
    }

    //Эндпоинт получения подборки событий по ID
    @GetMapping("/{compId}")
    public CompilationDto get(@PathVariable Long compId) {
        return compilationPublicService.get(compId);
    }
}