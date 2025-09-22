package ru.aleks.weather.check;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommonUsernames {

    public static final List<String> COMMON_USERNAMES = List.of(
            "gay",
            "sex",
            "gaysex",
            "gay-sex",
            "g@y-sex"
    );

    public boolean isContains(String username) {
        return COMMON_USERNAMES.contains(username.toLowerCase());
    }

}
