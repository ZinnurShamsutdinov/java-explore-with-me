package ru.practicum.main.exception;

/**
 * Класс собственного исключения при работе с датой и временем
 */
public class ValidationDateException extends RuntimeException {
    public ValidationDateException(String message) {
        super(message);
    }
}