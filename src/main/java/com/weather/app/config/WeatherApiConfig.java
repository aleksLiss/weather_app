package com.weather.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WeatherApiConfig {

    @Value("${spring.application.api.key}")
    private String token;

    public String getToken() {
        return token;
    }
}
