package ru.aleks.weather.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.aleks.weather.dto.SaveUserDto;
import ru.aleks.weather.model.Session;
import ru.aleks.weather.model.User;
import ru.aleks.weather.service.SessionService;
import ru.aleks.weather.service.UserService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Controller
public class UserController {

    private final UserService userService;
    private final SessionService sessionService;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private static final String NAMESESSION = "WEATHERAPPSESSIONID";

    public UserController(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @GetMapping("/user/up")
    public String getSignUpPage(Model model) {
        model.addAttribute("userDto", new SaveUserDto());
        return "sign/sign-up";
    }

    @PostMapping("/user/up")
    public String registerUser(@ModelAttribute SaveUserDto userDto, Model model) {
        Optional<User> savedUser = userService.save(userDto);
        if (savedUser.isEmpty()) {
            LOGGER.warn("UserController: error register new user");
            model.addAttribute("message", "Error saved user");
            return "errors/error";
        }
        LOGGER.info("UserController: user was successfully registered");
        return "index";
    }

    @GetMapping("/user/in")
    public String getSingInPage() {
        return "sign/sign-in";
    }

    @PostMapping("/user/in")
    public String signInUser(@ModelAttribute User user, Model model, HttpServletRequest request) {
        Optional<User> foundUser = userService.getUserByLogin(user.getLogin());
        if (foundUser.isEmpty()) {
            LOGGER.warn("UserController: User not found");
            model.addAttribute("message", "User not found");
            return "errors/error";
        }
        Cookie foundCookie = null;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(NAMESESSION)) {
                foundCookie = cookie;
            }
        }
        UUID randomSessionId = UUID.randomUUID();
        if (foundCookie == null) {
            foundCookie = new Cookie(NAMESESSION, randomSessionId.toString());
            foundCookie.setHttpOnly(true);
            foundCookie.setPath("/");
            foundCookie.setMaxAge(60 * 60);
        } else {
            UUID sessionId = UUID.fromString(foundCookie.getValue());
            Optional<Session> foundSession = sessionService.getById(sessionId);
            if (foundSession.isEmpty()) {
                if ((Duration.between(LocalDateTime.now(), foundSession.get().getExpiresAt()).toMillis() > 3600)) {
                    Session session = new Session();
                    session.setId(randomSessionId);
                    session.setUserId(user.getId());
                    session.setExpiresAt(LocalDateTime.now());
                    sessionService.save(session);
                }
                ;
            }
            model.addAttribute("user", user.getLogin());
            return "index";
        }
        return "index";
    }
}
