package ru.aleks.weather.exception;

public class FailedSaveSessionException extends RuntimeException {
    public FailedSaveSessionException(String message) {
        super(message);
    }
}
