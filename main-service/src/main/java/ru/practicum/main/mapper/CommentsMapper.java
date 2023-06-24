package ru.practicum.main.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main.entity.dto.comment.CommentDto;
import ru.practicum.main.entity.dto.comment.NewCommentDto;
import ru.practicum.main.entity.enums.CommentState;
import ru.practicum.main.entity.enums.CommentStateDto;
import ru.practicum.main.entity.models.Comment;
import ru.practicum.main.entity.models.Event;
import ru.practicum.main.entity.models.User;
import ru.practicum.main.exception.BadRequestException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Утилитарный класс CommentsMapper для преобразования Comment / NewCommentDto / CommentDto
 */
@UtilityClass
public class CommentsMapper {

    /**
     * Преобразование NewCommentDto в Comment
     *
     * @param newCommentDto Объект InputCommentDto
     * @param user            Объект user
     * @param event           Объект event
     * @return Преобразованный объект Comment
     */
    public Comment createComment(NewCommentDto newCommentDto, User user, Event event) {
        return Comment.builder()
                .text(newCommentDto.getText())
                .createdAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .commenter(user)
                .event(event)
                .state(CommentState.PUBLISHED)
                .build();
    }

    /**
     * Преобразование Comment в CommentDto
     *
     * @param comment Объект Comment
     * @return Преобразованный CommentDto
     */
    public CommentDto commentToCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .createdAt(comment.getCreatedAt())
                .commenter(UserMapper.userToUserShortDto(comment.getCommenter()))
                .event(EventMapper.eventToEventCommentDto(comment.getEvent()))
                .state(comment.getState().name())
                .build();

    }

    /**
     * Преобразование составных элементов владельца комментария (изменение комментария) в Comment
     *
     * @param commentId ID комментария
     * @param text      Текст комментария
     * @param user      Объект user
     * @param event     Объект event
     * @return Преобразованный Comment
     */
    public Comment updateComment(Long commentId, String text, User user, Event event) {
        return Comment.builder()
                .id(commentId)
                .text(text)
                .createdAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .commenter(user)
                .event(event)
                .state(CommentState.UPDATE)
                .build();
    }

    /**
     * Метод определения статуса комментария
     *
     * @param commentStateDto Объект CommentStateDto
     * @return Статус комментария
     */
    public CommentState toCommentState(CommentStateDto commentStateDto) {
        if (commentStateDto.name().equals(CommentState.UPDATE.name())) {
            return CommentState.UPDATE;
        }
        if (commentStateDto.name().equals(CommentState.PUBLISHED.name())) {
            return CommentState.PUBLISHED;
        }
        if (commentStateDto.name().equals(CommentState.CANCELED.name())) {
            return CommentState.CANCELED;
        } else {
            throw new BadRequestException("Статус не соответствует возможному: " + commentStateDto.name());
        }
    }
}