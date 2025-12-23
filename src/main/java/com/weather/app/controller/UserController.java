package com.weather.app.controller;

import com.weather.app.dto.*;
import com.weather.app.exception.*;
import com.weather.app.exception.password.IncorrectPasswordException;
import com.weather.app.exception.password.IncorrectRepeatPasswordException;
import com.weather.app.exception.password.PasswordContainsException;
import com.weather.app.exception.password.SmallLengthPasswordException;
import com.weather.app.exception.session.NameSessionIdNotFound;
import com.weather.app.exception.username.UserAlreadyExistsException;
import com.weather.app.exception.username.UserNotFoundException;
import com.weather.app.model.User;
import com.weather.app.service.SessionService;
import com.weather.app.service.UserService;
import com.weather.app.util.password.PasswordValidator;
import com.weather.app.util.password.RepeatPasswordValidator;
import com.weather.app.util.session.SessionDestroyer;
import com.weather.app.util.session.SessionFinder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/sign")
public class UserController {

    private final UserService userService;
    private final Environment environment;
    private final SessionService sessionService;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService, Environment environment, SessionService sessionService) {
        this.userService = userService;
        this.environment = environment;
        this.sessionService = sessionService;
    }

    @GetMapping("/in")
    public String getSignInPage(Model model) {
        model.addAttribute("signInUserDto", new SignInUserDto("", ""));
        return "sign-in/sign-in";
    }

    @PostMapping("/in")
    public String signInUser(@ModelAttribute SignInUserDto signInUserDto,
                             Model model,
                             HttpServletResponse response) {
        try {
            model.addAttribute("signInUserDto", new SignInUserDto("", ""));
            Optional<User> foundUser = userService.getUserByLogin(signInUserDto);
            sessionService.authenticateAndManageSession(
                    foundUser.get(),
                    response
            );
            return "redirect:/index";
        } catch (UserNotFoundException ex) {
            LOGGER.warn("User was not found");
            model.addAttribute("usernameError", new UsernameExceptionDto(ex.getMessage()));
            return "sign-in/sign-in-with-errors";
        } catch (IncorrectPasswordException ex) {
            LOGGER.warn("Incorrect input password");
            model.addAttribute("passwordError", new PasswordExceptionDto(ex.getMessage()));
            return "sign-in/sign-in-with-errors";
        } catch (RuntimeException ex) {
            LOGGER.warn("Unknown exception");
            model.addAttribute("error", ex.getMessage());
            return "error/error";
        }
    }

    @PostMapping("/out")
    public String signOutUser(HttpServletRequest request,
                              HttpServletResponse response,
                              Model model) {
        try {
            if (SessionFinder.findSession(request, environment, sessionService).isPresent()) {
                SessionDestroyer.invalidateSession(request, response, environment);
            }
            return "redirect:/sign/in";
        } catch (UserNotFoundException |
                 IncorrectPasswordException |
                 CookiesNotFoundException |
                 NameSessionIdNotFound ex) {
            LOGGER.warn("Exception out user");
            model.addAttribute("error", ex.getMessage());
            return "error/error";
        }
    }

    @GetMapping("/up")
    public String getSignUpPage(Model model) {
        model.addAttribute("saveUserDto", new SaveUserDto("", "", ""));
        return "sign-up/sign-up";
    }

    @PostMapping("/up")
    public String signUpUser(@ModelAttribute SaveUserDto saveUserDto,
                             Model model) {
        try {
            RepeatPasswordValidator.checkRepeatPasswordSignUpUser(saveUserDto);
            PasswordValidator.isSimple(saveUserDto.password());
            User user = new User(saveUserDto.login(), saveUserDto.password());
            userService.save(user);
        } catch (UserAlreadyExistsException ex) {
            LOGGER.warn("User already exists");
            model.addAttribute("usernameError", new UsernameExceptionDto(ex.getMessage()));
            model.addAttribute("saveUserDto", saveUserDto);
            return "sign-up/sign-up-with-errors";
        } catch (SmallLengthPasswordException | PasswordContainsException | IncorrectPasswordException ex) {
            LOGGER.warn("Exception input password");
            model.addAttribute("passwordError", new PasswordExceptionDto(ex.getMessage()));
            model.addAttribute("saveUserDto", saveUserDto);
            return "sign-up/sign-up-with-errors";
        } catch (IncorrectRepeatPasswordException ex) {
            LOGGER.warn("Exception input repeat password");
            model.addAttribute("repeatPasswordError", new RepeatPasswordDto(ex.getMessage()));
            model.addAttribute("saveUserDto", saveUserDto);
            return "sign-up/sign-up-with-errors";
        }
        return "redirect:/sign/in";
    }
}