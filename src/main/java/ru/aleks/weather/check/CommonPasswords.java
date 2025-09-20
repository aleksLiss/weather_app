package ru.aleks.weather.check;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommonPasswords {

    private static final List<String> COMMON_PASSWORDS = List.of(
            "123456",
            "password",
            "123456789",
            "12345",
            "12345678",
            "qwerty",
            "1234567",
            "111111",
            "123123",
            "abc123",
            "password1",
            "1234",
            "iloveyou",
            "1q2w3e4r",
            "admin",
            "letmein",
            "welcome",
            "monkey",
            "login",
            "starwars",
            "dragon",
            "passw0rd",
            "master",
            "hello",
            "freedom",
            "whatever",
            "qazwsx",
            "trustno1",
            "654321",
            "jordan23"
    );

    public boolean isContains(String psw) {
        return COMMON_PASSWORDS.contains(psw.toLowerCase());
    }
}

