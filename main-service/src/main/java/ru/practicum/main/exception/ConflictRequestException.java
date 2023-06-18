package ru.practicum.main.exception;

//Класс собственного исключения при работе с запросами
public class ConflictRequestException extends RuntimeException {
    public ConflictRequestException(String message) {
        super(message);
    }
}