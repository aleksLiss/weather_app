package ru.aleks.weather.exception;

public class IncorectCityNameException extends RuntimeException {
    public IncorectCityNameException(String message) {
        super(message);
    }
}
