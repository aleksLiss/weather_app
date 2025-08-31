package ru.aleks.weather.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.aleks.weather.dto.UserDto;
import ru.aleks.weather.model.User;

@Controller
@RequestMapping("/")
public class UserController {

    @GetMapping("user/in")
    public String getSignIn() {
        return "sign/sign-in";
    }

    @GetMapping("/user/up")
    public String getSignUpPage() {
        return "sign/sign-up";
    }

    @PostMapping
    public String registerUser(@ModelAttribute UserDto userDto) {
        System.out.println(userDto.getUsername());
        System.out.println(userDto.getPassword());
        System.out.println(userDto.getRepeatPassword());
        try {

        } catch (Exception ex) {
            return "errors/error";
        }
        return "index";
    }

}
