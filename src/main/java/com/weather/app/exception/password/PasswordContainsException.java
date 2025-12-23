package com.weather.app.exception.password;

public class PasswordContainsException extends RuntimeException {
    public PasswordContainsException(String message) {
        super(message);
    }
}
