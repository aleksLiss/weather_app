package com.weather.app.util.password;

import com.weather.app.dto.SaveUserDto;
import com.weather.app.exception.password.IncorrectRepeatPasswordException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PasswordValidatorTest {

    @Mock
    private SaveUserDto saveUserDto;

    @Test
    void whenPasswordEqualsRepeatPasswordThenOk() {
        saveUserDto = new SaveUserDto("login", "123", "123");
        assertThatCode(() -> RepeatPasswordValidator.checkRepeatPasswordSignUpUser(saveUserDto))
                .doesNotThrowAnyException();
    }

    @Test
    void whenPasswordNotEqualsRepeatPasswordThenThrowException() {
        saveUserDto = new SaveUserDto("login", "123", "!!!!!!!!!!!!!!!");
        assertThatThrownBy(() -> RepeatPasswordValidator.checkRepeatPasswordSignUpUser(saveUserDto))
                .isInstanceOf(IncorrectRepeatPasswordException.class)
                .hasMessage("Password must be equals repeat password");
    }
}