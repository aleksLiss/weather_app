package ru.aleks.weather.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.aleks.weather.utils.CheckRegisterUser;
import ru.aleks.weather.dto.SaveUserDto;
import ru.aleks.weather.exception.UserAlreadyExistsException;
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
    private final CheckRegisterUser checkRegisterUser;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private static final String NAMESESSION = "WEATHERAPPSESSIONID";

    public UserController(UserService userService, SessionService sessionService, CheckRegisterUser checkRegisterUser) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.checkRegisterUser = checkRegisterUser;
    }

    @GetMapping("/user/up")
    public String getSignUpPage(Model model) {
        model.addAttribute("userDto", new SaveUserDto());
        return "sign/sign-up";
    }

    @PostMapping("/user/up")
    public String registerUser(@ModelAttribute SaveUserDto userDto, Model model) {
        Optional<User> savedUser;
        checkRegisterUser.checkRegister(userDto);
        try {
            savedUser = userService.save(userDto);
        } catch (Exception ex ) {
            throw new UserAlreadyExistsException(ex.getMessage());
        }
        model.addAttribute("user", savedUser.get());
        return "/sign/sign-in";
    }

    @GetMapping("/login")
    public String getSingInPage(Model model) {
        model.addAttribute("user", new User());
        return "sign/sign-in";
    }

    @PostMapping("/login")
    public String signInUser(@ModelAttribute User user, Model model, HttpServletRequest request, HttpServletResponse response) {
        Optional<User> foundUserFromInputField = userService.getUserByLogin(user.getLogin());
        if (foundUserFromInputField.isEmpty()) {
            LOGGER.warn("UserController: user was not found");
            model.addAttribute("message", "User was not found");
            return "errors/error";
        }
        Optional<Cookie> foundCookie = getCookieFromRequest(request, NAMESESSION);
        if (foundCookie.isEmpty()) {
            LOGGER.warn("UserController: cookie was not found");
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
            return "redirect:/";
        }
        Optional<String> foundSessionId = getSessionIdFromCookie(foundCookie.get());
        if (foundSessionId.isEmpty()) {
            LOGGER.warn("UserController: session id was not found");
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
            return "redirect:/";
        }
        Optional<Session> foundSession = sessionService.getById(UUID.fromString(foundSessionId.get()));
        if (foundSession.isEmpty()) {
            LOGGER.warn("UserController: session was not found");
            Optional<Session> createdSession = saveNewSession(user.getId());
            if (createdSession.isPresent()) {
                sessionService.save(createdSession.get());
                Optional<Cookie> createdSessionCookie = createNewCookieForSession(createdSession.get().getId());
                Optional<Cookie> createdUserCookie = createNewCookieForUser(user.getLogin());
                response.addCookie(createdUserCookie.get());
                response.addCookie(createdSessionCookie.get());
                return "redirect:/";
            }
        }
        Optional<User> foundUserFromSession = userService.getUserByUserId(foundSession.get().getUserId());
        if (!foundUserFromSession.get().getLogin().equals(foundUserFromInputField.get().getLogin())) {
            sessionService.isDeleted(UUID.fromString(foundSessionId.get()));
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
            return "redirect:/";
        }
        LOGGER.info("UserController: user login from session {}", userService.getUserByUserId(foundSession.get().getUserId()).get().getLogin());
        LOGGER.info("UserController: user login from input {}", user.getLogin());
        LocalDateTime sessionTime = foundSession.get().getExpiresAt();
        LocalDateTime now = LocalDateTime.now();
        if (Duration.between(sessionTime, now).toSeconds() > 3600) {
            Optional<Session> createdSession = saveNewSession(foundSession.get().getUserId());
            if (createdSession.isPresent()) {
                sessionService.save(createdSession.get());
                Optional<Cookie> createdSessionCookie = createNewCookieForSession(createdSession.get().getId());
                Optional<Cookie> createdUserCookie = createNewCookieForUser(user.getLogin());
                response.addCookie(createdUserCookie.get());
                response.addCookie(createdSessionCookie.get());
            }
        }
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String signOutUser(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = request.getSession();
            session.invalidate();
            Optional<Cookie> foundSessionCookieByName = getCookieFromRequest(request, NAMESESSION);
            Optional<Cookie> foundUsernameCookieByName = getCookieFromRequest(request, "username");
            foundUsernameCookieByName.ifPresent(cookie -> deleteCookie(request, response, cookie));
            foundSessionCookieByName.ifPresent(cookie -> deleteCookie(request, response, cookie));
            Optional<String> foundSessionId = getSessionIdFromCookie(foundSessionCookieByName.get());
            sessionService.isDeleted(UUID.fromString(foundSessionId.get()));
            LOGGER.info("UserController: session was invalidated");
        } catch (Exception exception) {
            LOGGER.warn("UserController: session was not invalidated");
        }
        return "redirect:/login";
    }

    private void deleteCookie(HttpServletRequest request, HttpServletResponse response, Cookie cookie) {
        Cookie[] cookies = request.getCookies();
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals(cookie.getName())) {
                cookies[i].setPath("/");
                cookies[i].setMaxAge(0);
                cookies[i].setValue("");
                response.addCookie(cookies[i]);
                LOGGER.info("UserController: cookie with name '{}' was deleted", cookies[i].getName());
            }
        }
    }

    private Optional<Session> saveNewSession(int userId) {
        Optional<Session> newSession = Optional.empty();
        try {
            newSession = Optional.of(
                    new Session(
                            UUID.randomUUID(),
                            userId,
                            LocalDateTime.now()
                    )
            );
        } catch (Exception ex) {
            LOGGER.warn("UserController: new session was not saved");
        }
        return newSession;
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

    private Optional<Cookie> getCookieFromRequest(HttpServletRequest request, String nameCookie) {
        Optional<Cookie> foundCookie = Optional.empty();
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(nameCookie)) {
                    foundCookie = Optional.of(
                            cookie
                    );
                }
            }
        }
        return foundCookie;
    }
}
