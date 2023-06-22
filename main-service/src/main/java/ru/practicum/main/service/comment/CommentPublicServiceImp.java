package ru.practicum.main.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.entity.dto.comment.CommentDto;
import ru.practicum.main.entity.enums.CommentState;
import ru.practicum.main.entity.models.Comment;
import ru.practicum.main.entity.models.Event;
import ru.practicum.main.mapper.CommentsMapper;
import ru.practicum.main.repository.CommentRepository;
import ru.practicum.main.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс CommentsPublicServiceImp для отработки логики запросов и логирования
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentPublicServiceImp implements CommentPublicService {

    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<CommentDto> get(Long id, Integer from, Integer size) {
        Event event = eventRepository.get(id);
        Pageable pageable = PageRequest.of(from, size);
        List<Comment> comments = commentRepository.findByEventAndStateIsNot(event, CommentState.CANCELED, pageable);
        log.info("Получен публичный запрос на список всех комментариев по событию с id: {}, from: {}, size {}", id, from, size);
        return comments.stream().map(CommentsMapper::commentToCommentDto).collect(Collectors.toList());
    }
}