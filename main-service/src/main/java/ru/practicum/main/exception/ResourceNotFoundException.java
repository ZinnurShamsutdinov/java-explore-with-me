package ru.practicum.main.exception;

/**
 * Класс собственного исключения при работе с объектом который не найден
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}