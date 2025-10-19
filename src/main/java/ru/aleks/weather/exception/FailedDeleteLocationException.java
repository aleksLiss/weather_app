package ru.aleks.weather.exception;

public class FailedDeleteLocationException extends RuntimeException {
    public FailedDeleteLocationException(String message) {
        super(message);
    }
}
