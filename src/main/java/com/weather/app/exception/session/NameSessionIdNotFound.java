package com.weather.app.exception.session;

public class NameSessionIdNotFound extends RuntimeException {
    public NameSessionIdNotFound(String message) {
        super(message);
    }
}
