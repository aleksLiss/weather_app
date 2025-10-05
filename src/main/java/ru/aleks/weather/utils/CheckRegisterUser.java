package ru.aleks.weather.utils;

import org.springframework.stereotype.Component;
import ru.aleks.weather.dto.SaveUserDto;
import ru.aleks.weather.exception.IncorrectPasswordException;
import ru.aleks.weather.exception.IncorrectUsernameException;

@Component
public class CheckRegisterUser {

    private final CommonPasswords commonPasswords;
    private final CommonUsernames commonUsernames;

    public CheckRegisterUser(CommonPasswords commonPasswords, CommonUsernames commonUsernames) {
        this.commonPasswords = commonPasswords;
        this.commonUsernames = commonUsernames;
    }

    public String checkRegister(SaveUserDto saveUserDto) {
        String exUsernameMsg = checkValidUsername(saveUserDto.getUsername());
        if (!"valid".equals(exUsernameMsg)) {
            throw new IncorrectUsernameException(exUsernameMsg);
        };
        String exPasswordMsg = checkValidPassword(saveUserDto.getUsername(), saveUserDto.getPassword());
        if (!"valid".equals(exPasswordMsg)) {
            throw new IncorrectPasswordException(exPasswordMsg);
        };
        String exRepeatPasswordMsg = checkValidRepeatPassword(saveUserDto.getPassword(), saveUserDto.getRepeatPassword());
        if (!"valid".equals(exRepeatPasswordMsg)) {
            throw new IncorrectPasswordException(exRepeatPasswordMsg);
        };
        return "valid";
    }

    private String checkValidUsername(String username) {
        return commonUsernames.isContains(username.toLowerCase()) ? "no gay-sex names in this app =)" : "valid";
    }

    private String checkValidPassword(String username, String password) {
        if (username.equalsIgnoreCase(password)) {
            return "password must be not equals username";
        }
        if (commonPasswords.isContains(password.toLowerCase())) {
            return "you password is a common one";
        }
        return "valid";
    }

    private String checkValidRepeatPassword(String password, String repeatPassword) {
        return password.equalsIgnoreCase(repeatPassword) ? "valid" : "password not equal repeat password";
    }
}
