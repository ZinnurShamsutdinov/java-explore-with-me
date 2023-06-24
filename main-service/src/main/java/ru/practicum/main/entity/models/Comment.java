package ru.practicum.main.entity.models;

import lombok.*;
import ru.practicum.main.entity.enums.CommentState;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Модель объекта Comment (Комментарий)
 */
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "text")
    private String text;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "commenter_id")
    private User commenter;
    @ManyToOne
    @JoinColumn(name = "commented_event_id", referencedColumnName = "id")
    private Event event;
    @Enumerated(EnumType.STRING)
    private CommentState state;
}