package ru.aleks.weather.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;


@Configuration
@Component
@Profile("!test")
@PropertySource("classpath:application.properties")
public class PostgresDatabaseConfig {


    @Autowired
    Environment environment;

    private final String URL = "spring.datasource.url";
    private final String USERNAME = "spring.datasource.username";
    private final String PASSWORD = "spring.datasource.password";
    private final String DRIVER = "spring.datasource.driver-class-name";


    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setUrl(environment.getProperty(URL));
        driverManagerDataSource.setUsername(environment.getProperty(USERNAME));
        driverManagerDataSource.setPassword(environment.getProperty(PASSWORD));
        driverManagerDataSource.setDriverClassName(environment.getProperty(DRIVER));
        return driverManagerDataSource;
    }
}