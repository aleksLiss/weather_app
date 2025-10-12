package ru.aleks.weather.exception;

public class FailGetBodyFromResponseException extends RuntimeException {
    public FailGetBodyFromResponseException(String message) {
        super(message);
    }
}
