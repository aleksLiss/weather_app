package ru.aleks.weather.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import ru.aleks.weather.exception.UserNotFoundException;
import ru.aleks.weather.model.User;
import ru.aleks.weather.service.UserService;

import java.util.Optional;
@Component
public class AuthenticateUser {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticateUser.class);
    private final UserService userService;

    public AuthenticateUser(UserService userService) {
        this.userService = userService;
    }

    private String authenticateUser(HttpServletRequest request,
                                    Model model) {
        Cookie[] cookies = request.getCookies();
        String foundUserName = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("username")) {
                foundUserName = cookie.getValue();
            }
        }
        if (foundUserName != null) {
            model.addAttribute("username", foundUserName);
        }
        return foundUserName;
    }

    public Optional<User> findUser(HttpServletRequest request,
                                   Model model) {
        String username = authenticateUser(request, model);
        return userService.getUserByLogin(username);
    }
}
