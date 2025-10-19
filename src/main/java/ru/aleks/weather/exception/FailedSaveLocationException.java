package ru.aleks.weather.exception;

public class FailedSaveLocationException extends RuntimeException {
    public FailedSaveLocationException(String message) {
        super(message);
    }
}
