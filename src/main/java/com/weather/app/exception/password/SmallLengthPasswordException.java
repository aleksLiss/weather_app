package com.weather.app.exception.password;

public class SmallLengthPasswordException extends RuntimeException {
    public SmallLengthPasswordException(String message) {
        super(message);
    }
}
