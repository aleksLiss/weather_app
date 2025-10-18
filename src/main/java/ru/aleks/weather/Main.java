package ru.aleks.weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
// todo add try catch exception if not correct city or coordinates

// todo check time session obviously
//  time out and check in different browsers

// todo fix display images in index page

// todo improve code
// todo delete unnecessaries dto's

// todo create tests for another services and controllers