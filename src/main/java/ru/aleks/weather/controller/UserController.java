package ru.aleks.weather.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.aleks.weather.dto.UserDto;
import ru.aleks.weather.model.User;

@Controller
public class UserController {

    @GetMapping("/user/up")
    public String getSignUpPage(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "sign/sign-up";
    }

    @PostMapping("/user/up")
    public String registerUser(@ModelAttribute UserDto userDto, Model model) {

        UserDto registeredUser = userDto;

        model.addAttribute("message", userDto.getUsername());
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
