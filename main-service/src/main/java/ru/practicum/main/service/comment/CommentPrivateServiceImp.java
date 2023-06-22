package ru.practicum.main.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.entity.dto.comment.CommentDto;
import ru.practicum.main.entity.dto.comment.NewCommentDto;
import ru.practicum.main.entity.enums.CommentState;
import ru.practicum.main.entity.models.Comment;
import ru.practicum.main.entity.models.Event;
import ru.practicum.main.entity.models.User;
import ru.practicum.main.exception.BadRequestException;
import ru.practicum.main.exception.ForbiddenEventException;
import ru.practicum.main.mapper.CommentsMapper;
import ru.practicum.main.repository.CommentRepository;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс CommentsPrivateServiceImp для отработки логики запросов и логирования
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentPrivateServiceImp implements CommentPrivateService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;

    @Override
    public CommentDto get(Long commentId, Long userId) {
        Comment comment = commentRepository.get(commentId);
        User user = userRepository.get(userId);
        checkCommentOnOwner(comment, user);
        log.info("Получен приватный запрос на поиск commentId: {}, userId: {}", commentId, userId);
        return CommentsMapper.commentToCommentDto(comment);
    }

    @Override
    public List<CommentDto> get(Long eventId, Long userId, Integer from, Integer size) {
        Event event = eventRepository.get(eventId);
        User user = userRepository.get(userId);
        Pageable pageable = PageRequest.of(from, size);
        List<Comment> comments = commentRepository.findByEventAndCommenter(event, user, pageable);
        log.info("Получен приватный запрос на список всех комментариев по eventId: {}, userId: {}, from: {}, size {}",
                eventId, userId, from, size);
        return comments.stream().map(CommentsMapper::commentToCommentDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto create(NewCommentDto newCommentDto) {
        Event event = eventRepository.get(newCommentDto.getEventId());
        User user = userRepository.get(newCommentDto.getUserId());
        Comment comment = CommentsMapper.createComment(newCommentDto, user, event);
        log.info("Получен приватный запрос на добавления комментария к eventId: {}, userId: {}",
                newCommentDto.getEventId(), newCommentDto.getUserId());
        return CommentsMapper.commentToCommentDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentDto update(Long commentId, NewCommentDto newCommentDto) {
        Comment comment = commentRepository.get(commentId);
        if (comment.getState().equals(CommentState.CANCELED)) {
            throw new BadRequestException("Комментарий с id:" + comment.getId() + " ранее был отменен");
        }
        Event event = eventRepository.get(newCommentDto.getEventId());
        User user = userRepository.get(newCommentDto.getUserId());
        if (!comment.getEvent().getId().equals(event.getId())) {
            throw new ForbiddenEventException("Комментарий с id: " + comment.getId()
                    + " не принадлежит событию с id: " + event.getId());
        }
        checkCommentOnOwner(comment, user);
        Comment newComment = CommentsMapper.updateComment(comment.getId(), newCommentDto.getText(), user, event);
        log.info("Получен приватный запрос на изменения комментария к commentId: {}, eventId: {}", commentId, event.getId());
        return CommentsMapper.commentToCommentDto(commentRepository.save(newComment));
    }

    @Override
    @Transactional
    public void delete(Long commentId, Long userId) {
        User user = userRepository.get(userId);
        Comment comment = commentRepository.get(commentId);
        checkCommentOnOwner(comment, user);
        log.info("Получен приватный запрос на удаления комментария к commentId: {}, userId: {}", commentId, userId);
        commentRepository.delete(comment);
    }

    /**
     * Метод проверки комментария на принадлежность его пользователю
     *
     * @param comment Объект Comment
     * @param user    Объект User
     */
    public void checkCommentOnOwner(Comment comment, User user) {
        if (!comment.getCommenter().getId().equals(user.getId())) {
            throw new ForbiddenEventException("Комментарий с id:" + comment.getId()
                    + " не принадлежит пользователю с id:" + user.getId());
        }
    }
}
