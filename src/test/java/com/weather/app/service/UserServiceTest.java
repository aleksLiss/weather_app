package com.weather.app.service;

import com.weather.app.dto.SignInUserDto;
import com.weather.app.exception.password.IncorrectPasswordException;
import com.weather.app.exception.username.UserNotFoundException;
import com.weather.app.model.User;
import com.weather.app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private User user;
    @InjectMocks
    private UserService userService;

    @Test
    void whenInputPasswordCorrectThenOk() {
        String inputPassword = "123";
        String hashed = BCrypt.hashpw(inputPassword, BCrypt.gensalt());
        assertThatCode(() -> userService.checkCorrectPassword(inputPassword, hashed))
                .doesNotThrowAnyException();
    }

    @Test
    void whenInputPasswordNotCorrectThenThrowException() {
        String password = "123";
        String wrongPassword = "444";
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        assertThatThrownBy(() -> userService.checkCorrectPassword(wrongPassword, hashed))
                .isInstanceOf(IncorrectPasswordException.class)
                .hasMessage("Input password not correct");
    }

    @Test
    void checkCorrectPassword_ShouldThrowException_WhenHashIsInvalid() {
        String password = "password";
        String invalidHash = "$2a$12$invalidHashFormat";
        assertThatThrownBy(() -> userService.checkCorrectPassword(password, invalidHash))
                .isInstanceOf(StringIndexOutOfBoundsException.class);
    }

    @Test
    void whenSaveUserThenOk() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        Optional<User> savedUser = userService.save(user);
        assertThat(savedUser).isPresent();
    }

    @Test
    void whenSaveUserAndThisLoginAlreadyExistsThenTrowException() {
        User user = new User();
        user.setLogin("login");
        String hashedPassword = BCrypt.hashpw("123", BCrypt.gensalt());
        user.setPassword(hashedPassword);
        when(userRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);
        assertThatThrownBy(() -> userRepository.save(user))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void whenSaveUserThenAndGetByLoginThenReturnSavedUser() {
        User user = new User();
        user.setLogin("login");
        String hashedPassword = BCrypt.hashpw("123", BCrypt.gensalt());
        user.setPassword(hashedPassword);
        SignInUserDto signInUserDto = new SignInUserDto("login", "123");
        when(userRepository.getUserByLogin(user.getLogin())).thenReturn(Optional.of(user));
        Optional<User> foundUser = userService.getUserByLogin(signInUserDto);
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get()).isEqualTo(user);
    }

    @Test
    void whenDontSaveUserThenAndGetByLoginThenThrowException() {
        SignInUserDto signInUserDto = new SignInUserDto("login", "123");
        when(userRepository.getUserByLogin(any(String.class))).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.getUserByLogin(signInUserDto))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User by login was not found");
    }

    @Test
    void whenSaveUserAndDeleteItByIdThenOk() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        int id = user.getId();
        userService.save(user);
        doNothing()
                .when(userRepository)
                .deleteById(anyInt());
        userService.deleteById(id);
        verify(userRepository, times(1))
                .deleteById(eq(id));
    }
}