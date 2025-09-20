package ru.aleks.weather.exception;

public class IncorrectUsernameException extends RuntimeException {
    public IncorrectUsernameException(String message) {
        super(message);
    }
}
