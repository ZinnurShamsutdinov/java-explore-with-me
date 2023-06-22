package ru.practicum.main.entity.dto;

import lombok.*;
import ru.practicum.main.entity.dto.request.ParticipationRequestDto;

import java.util.List;

/**
 * Модель объекта EventRequestStatusUpdateResult
 * (Результат подтверждения/отклонения заявок на участие в событии)
 */
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;
}