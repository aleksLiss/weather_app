package com.weather.app.util.password;

import com.weather.app.exception.password.PasswordContainsException;
import com.weather.app.exception.password.SmallLengthPasswordException;

public class PasswordValidator {

    private PasswordValidator() {
    }

    public static void isSimple(String password) {
        if (password == null || password.length() < 8) {
            throw new SmallLengthPasswordException(
                    "The password length must be more than 8 characters");
        }
        String strongPasswordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$";
        if (!password.matches(strongPasswordRegex)) {
            throw new PasswordContainsException("The password must be contains one number, one upper- and lowercase letter and one symbol");
        }
    }
}
