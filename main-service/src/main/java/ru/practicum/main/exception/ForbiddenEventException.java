package ru.practicum.main.exception;

/**
 * Класс собственного исключения при работе с недоступным/запрещённым объектом
 */
public class ForbiddenEventException extends RuntimeException {
    public ForbiddenEventException(String message) {
        super(message);
    }
}