package com.weather.app.exception.password;

public class IncorrectRepeatPasswordException extends RuntimeException {
    public IncorrectRepeatPasswordException(String message) {
        super(message);
    }
}
