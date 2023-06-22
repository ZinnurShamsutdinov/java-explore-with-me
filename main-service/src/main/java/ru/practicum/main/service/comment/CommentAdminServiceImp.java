package ru.practicum.main.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.entity.dto.comment.CommentDto;
import ru.practicum.main.entity.dto.comment.NewCommentDto;
import ru.practicum.main.entity.dto.comment.UpdateCommentDto;
import ru.practicum.main.entity.models.Comment;
import ru.practicum.main.entity.models.Event;
import ru.practicum.main.entity.models.User;
import ru.practicum.main.exception.ForbiddenEventException;
import ru.practicum.main.exception.ResourceNotFoundException;
import ru.practicum.main.mapper.CommentsMapper;
import ru.practicum.main.repository.CommentRepository;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс CommentsAdminServiceImp для отработки логики запросов и логирования
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentAdminServiceImp implements CommentAdminService {

    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    public CommentDto get(Long id) {
        log.info("Получен запрос на поиск комментария по id: {}", id);
        return CommentsMapper.commentToCommentDto(commentRepository.get(id));
    }

    @Override
    public List<CommentDto> get(Long id, Integer from, Integer size) {
        Event event = eventRepository.get(id);
        Pageable pageable = PageRequest.of(from, size);
        List<Comment> comments = commentRepository.findByEvent(event, pageable);
        log.info("Получен запрос на список всех комментариев по событию с id: {}, from: {}, size {}", id, from, size);
        return comments.stream().map(CommentsMapper::commentToCommentDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto create(NewCommentDto newCommentDto) {
        Event event = eventRepository.get(newCommentDto.getEventId());
        User user = userRepository.get(newCommentDto.getUserId());
        Comment comment = CommentsMapper.createComment(newCommentDto, user, event);
        log.info("Получен запрос на добавления комментария к eventId: {}, userId: {}",
                newCommentDto.getEventId(), newCommentDto.getUserId());
        return CommentsMapper.commentToCommentDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentDto update(Long id, UpdateCommentDto updateComment) {
        Event event = eventRepository.get(updateComment.getEventId());
        if (!userRepository.existsById(updateComment.getUserId())) {
            throw new ResourceNotFoundException("Пользователь c id: " + updateComment.getUserId() + " не найден");
        }
        Comment comment = commentRepository.get(id);
        if (!comment.getEvent().getId().equals(event.getId())) {
            throw new ForbiddenEventException("Комментарий с id: " + comment.getId()
                    + " не принадлежит событию с id: " + event.getId());
        }
        if (updateComment.getText() != null && !updateComment.getText().isBlank()) {
            comment.setText(updateComment.getText());
        }
        if (updateComment.getCommentStateDto() != null) {
            comment.setState(CommentsMapper.toCommentState(updateComment.getCommentStateDto()));
        }
        log.info("Получен запрос на изменения комментария к commentId: {}, eventId: {}", id, event.getId());
        return CommentsMapper.commentToCommentDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Comment comment = commentRepository.get(id);
        log.info("Получен запрос на удаления комментария к commentId: {}", id);
        commentRepository.delete(comment);
    }
}