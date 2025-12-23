package com.weather.app.util.password;

import com.weather.app.dto.SaveUserDto;
import com.weather.app.exception.password.IncorrectRepeatPasswordException;

public class RepeatPasswordValidator {

    private RepeatPasswordValidator() {
    }

    public static void checkRepeatPasswordSignUpUser(SaveUserDto saveUserDto) {
        if (!saveUserDto.password().equals(saveUserDto.repeatPassword())) {
            throw new IncorrectRepeatPasswordException("Password must be equals repeat password");
        }
    }
}
