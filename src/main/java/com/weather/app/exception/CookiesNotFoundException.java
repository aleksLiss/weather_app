package com.weather.app.exception;

public class CookiesNotFoundException extends RuntimeException {
    public CookiesNotFoundException(String message) {
        super(message);
    }
}
