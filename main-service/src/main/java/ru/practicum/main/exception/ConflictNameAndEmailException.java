package ru.practicum.main.exception;

/**
 * Класс собственного исключения при работе с данными пользователя Name / E-Mail
 */
public class ConflictNameAndEmailException extends RuntimeException {
    public ConflictNameAndEmailException(String message) {
        super(message);
    }
}