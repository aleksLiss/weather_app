package ru.aleks.weather.exception;

public class FailedTransformJsonToDto extends RuntimeException {
    public FailedTransformJsonToDto(String message) {
        super(message);
    }
}
