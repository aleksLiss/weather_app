package ru.aleks.weather.controller;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.aleks.weather.dto.SaveUserDto;
import ru.aleks.weather.model.User;
import ru.aleks.weather.service.UserService;

import java.util.Optional;

@Controller
public class UserController {

    // todo create integrate test
    // todo create httpsession logic

    private final UserService userService;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/up")
    public String getSignUpPage(Model model) {
        model.addAttribute("userDto", new SaveUserDto());
        return "sign/sign-up";
    }

    @PostMapping("/user/up")
    public String registerUser(@ModelAttribute SaveUserDto userDto, Model model, HttpSession session) {
        Optional<User> savedUser = userService.save(userDto);
        if (savedUser.isEmpty()) {
            LOGGER.warn("UserController: error register new user");
            model.addAttribute("message", "Error saved user");
            return "errors/error";
        }
        LOGGER.info("UserController: user was successfully registered");
        session.setAttribute("user", savedUser.get());
        return "index";
    }

    @GetMapping("/user/in")
    public String getSingInPage(Model model) {
        model.addAttribute("user", new User());
        return "sign/sign-in";
    }

    @PostMapping("/user/in")
    public String signInUser(@ModelAttribute User user) {
        try {

        } catch (Exception ex) {

        }
        return "index";
    }

}
