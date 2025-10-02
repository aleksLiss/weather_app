package ru.aleks.weather.exception;

public class IncorrectCoordinatesException extends RuntimeException {
    public IncorrectCoordinatesException(String message) {
        super(message);
    }
}
