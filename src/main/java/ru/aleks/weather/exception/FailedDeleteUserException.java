package ru.aleks.weather.exception;

public class FailedDeleteUserException extends RuntimeException {
    public FailedDeleteUserException(String message) {
        super(message);
    }
}
