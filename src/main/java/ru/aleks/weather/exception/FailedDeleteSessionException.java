package ru.aleks.weather.exception;

public class FailedDeleteSessionException extends RuntimeException {
    public FailedDeleteSessionException(String message) {
        super(message);
    }
}
