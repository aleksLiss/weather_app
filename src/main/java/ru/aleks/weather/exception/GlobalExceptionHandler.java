package ru.aleks.weather.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IncorrectUsernameException.class)
    public String handeIncorrectUsernameException(Exception ex, Model model) {
        LOGGER.info("GlobalExceptionHandler: incorrect username");
        model.addAttribute("username", ex.getMessage());
        return "/sign/sign-up-with-errors";
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public String handleIncorrectPasswordException(Exception ex, Model model) {
        LOGGER.info("GlobalExceptionHandler: incorrect password");
        model.addAttribute("password", ex.getMessage());
        return "/sign/sign-up-with-errors";
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public String handleUserAlreadyExistsException(Exception ex, Model model) {
        LOGGER.info("GlobalExceptionHandler: user already exists");
        model.addAttribute("username", ex.getMessage());
        return "/sign/sign-up-with-errors";
    }
}
