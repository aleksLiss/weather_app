package ru.aleks.weather.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:application.properties")
public class TokenConfig {

    @Autowired
    Environment environment;

    private final String token = "weather.api.token";

    public String getToken() {
        return environment.getProperty(token);
    }
}
