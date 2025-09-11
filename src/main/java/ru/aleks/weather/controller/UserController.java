package ru.aleks.weather.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    @GetMapping("/login")
    public String getSingInPage(Model model) {
        model.addAttribute("user", new User());
        return "sign/sign-in";
    }

    @PostMapping("/login")
    public String signInUser(@ModelAttribute User user, Model model, HttpServletRequest request, HttpServletResponse response) {
        Optional<User> foundUser = userService.getUserByLogin(user.getLogin());
        if (foundUser.isEmpty()) {
            LOGGER.warn("UserController: user was not found");
            model.addAttribute("message", "User was not found");
            return "errors/error";
        }
        Optional<Cookie> foundCookie = getCookieFromRequest(request);
        if (foundCookie.isEmpty()) {
            Session session = new Session(
                    UUID.randomUUID(),
                    userService.getUserByLogin(user.getLogin()).get().getId(),
                    LocalDateTime.now()
            );
            sessionService.save(session);
            Optional<Cookie> createdSessionCookie = createNewCookieForSession(session.getId());
            Optional<Cookie> createdUserCookie = createNewCookieForUser(user.getLogin());
            response.addCookie(createdUserCookie.get());
            response.addCookie(createdSessionCookie.get());
            request.getSession().setAttribute("username", user.getLogin());
            return "/";
        }
        Optional<String> foundSessionId = getSessionIdFromCookie(foundCookie.get());
        if (foundSessionId.isEmpty()) {
            Session session = new Session(
                    UUID.randomUUID(),
                    userService.getUserByLogin(user.getLogin()).get().getId(),
                    LocalDateTime.now()
            );
            sessionService.save(session);
            Optional<Cookie> createdSessionCookie = createNewCookieForSession(session.getId());
            Optional<Cookie> createdUserCookie = createNewCookieForUser(user.getLogin());
            response.addCookie(createdUserCookie.get());
            response.addCookie(createdSessionCookie.get());
            request.getSession().setAttribute("username", user.getLogin());
            return "/";
        }
        Optional<Session> foundSession = sessionService.getById(UUID.fromString(foundSessionId.get()));
        if (foundSession.isEmpty()) {
            Session session = new Session(
                    UUID.randomUUID(),
                    userService.getUserByLogin(user.getLogin()).get().getId(),
                    LocalDateTime.now()
            );
            sessionService.save(session);
            Optional<Cookie> createdSessionCookie = createNewCookieForSession(session.getId());
            Optional<Cookie> createdUserCookie = createNewCookieForUser(user.getLogin());
            response.addCookie(createdUserCookie.get());
            response.addCookie(createdSessionCookie.get());
            request.getSession().setAttribute("username", user.getLogin());
            return "/";
        }
        LocalDateTime sessionTime = foundSession.get().getExpiresAt();
        LocalDateTime now = LocalDateTime.now();
        if (Duration.between(sessionTime, now).toMillis() > 3600) {
            Session session = new Session(
                    UUID.randomUUID(),
                    userService.getUserByLogin(user.getLogin()).get().getId(),
                    LocalDateTime.now()
            );
            sessionService.save(session);
            Optional<Cookie> createdSessionCookie = createNewCookieForSession(session.getId());
            Optional<Cookie> createdUserCookie = createNewCookieForUser(user.getLogin());
            response.addCookie(createdUserCookie.get());
            response.addCookie(createdSessionCookie.get());
            model.addAttribute("user", user.getLogin());
            return "redirect:/";
        }
        model.addAttribute("username", user.getLogin());
        return "/";
    }
    private Optional<Cookie> createNewCookieForUser(String login) {
        Cookie userCookie = new Cookie("username", userService.getUserByLogin(login).get().getLogin());
        userCookie.setHttpOnly(true);
        userCookie.setPath("/");
        userCookie.setMaxAge(60 * 60);
        return Optional.of(userCookie);
    }

    private Optional<Cookie> createNewCookieForSession(UUID sessionId) {
        Optional<Cookie> createdCookie = Optional.empty();
        try {
            Cookie cookie = new Cookie(NAMESESSION, String.valueOf(sessionId));
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60);
            createdCookie = Optional.of(cookie);
        } catch (Exception exception) {
            LOGGER.warn("UserController: error create new cookie");
        }
        return createdCookie;
    }

    private Optional<String> getSessionIdFromCookie(Cookie cookie) {
        Optional<String> foundSessionId = Optional.empty();
        if (cookie.getName().equals(NAMESESSION)) {
            foundSessionId = Optional.ofNullable(
                    cookie.getValue()
            );
        }
        return foundSessionId;
    }

    private Optional<Cookie> getCookieFromRequest(HttpServletRequest request) {
        Optional<Cookie> foundCookie = Optional.empty();
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(NAMESESSION)) {
                    foundCookie = Optional.of(
                            cookie
                    );
                }
            }
        }
        return foundCookie;
    }
}
