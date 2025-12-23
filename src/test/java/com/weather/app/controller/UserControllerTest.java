package com.weather.app.controller;

import com.weather.app.dto.*;
import com.weather.app.exception.password.IncorrectPasswordException;
import com.weather.app.exception.password.IncorrectRepeatPasswordException;
import com.weather.app.exception.password.PasswordContainsException;
import com.weather.app.exception.password.SmallLengthPasswordException;
import com.weather.app.exception.username.UserAlreadyExistsException;
import com.weather.app.exception.username.UserNotFoundException;
import com.weather.app.model.Session;
import com.weather.app.model.User;
import com.weather.app.service.SessionService;
import com.weather.app.service.UserService;
import com.weather.app.util.password.PasswordValidator;
import com.weather.app.util.password.RepeatPasswordValidator;
import com.weather.app.util.session.SessionDestroyer;
import com.weather.app.util.session.SessionFinder;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private SessionService sessionService;
    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void whenSignUpUserAndRepeatPasswordNotCorrectThenThrowException() throws Exception {
        when(userService.save(any(User.class))).thenThrow(IncorrectRepeatPasswordException.class);
        try (MockedStatic<RepeatPasswordValidator> repeatPasswordValidatorMockedStatic = mockStatic(RepeatPasswordValidator.class);
             MockedStatic<PasswordValidator> passwordValidatorMockedStatic = mockStatic(PasswordValidator.class)) {
            repeatPasswordValidatorMockedStatic.when(() -> RepeatPasswordValidator
                            .checkRepeatPasswordSignUpUser(any(SaveUserDto.class)))
                    .thenAnswer(invocation -> null);
            passwordValidatorMockedStatic.when(() -> PasswordValidator.isSimple(any(String.class)))
                    .thenAnswer(invocation -> null);
            mockMvc.perform(post("/sign/up"))
                    .andExpect(model().attributeExists("repeatPasswordError", "saveUserDto"))
                    .andExpect(view().name("sign-up/sign-up-with-errors"));
        }
        verify(userService, times(1)).save(any(User.class));
    }

    @Test
    void whenSignUpUserAndPasswordNotCorrectThenThrowException() throws Exception {
        when(userService.save(any(User.class))).thenThrow(IncorrectPasswordException.class);
        try (MockedStatic<RepeatPasswordValidator> repeatPasswordValidatorMockedStatic = mockStatic(RepeatPasswordValidator.class);
             MockedStatic<PasswordValidator> passwordValidatorMockedStatic = mockStatic(PasswordValidator.class)) {
            repeatPasswordValidatorMockedStatic.when(() -> RepeatPasswordValidator
                            .checkRepeatPasswordSignUpUser(any(SaveUserDto.class)))
                    .thenAnswer(invocation -> null);
            passwordValidatorMockedStatic.when(() -> PasswordValidator.isSimple(any(String.class)))
                    .thenAnswer(invocation -> null);
            mockMvc.perform(post("/sign/up"))
                    .andExpect(model().attributeExists("passwordError", "saveUserDto"))
                    .andExpect(view().name("sign-up/sign-up-with-errors"));
        }
        verify(userService, times(1)).save(any(User.class));
    }

    @Test
    void whenSignUpUserAndPasswordNotContainsMinimalCharactersThenThrowException() throws Exception {
        when(userService.save(any(User.class))).thenThrow(PasswordContainsException.class);
        try (MockedStatic<RepeatPasswordValidator> repeatPasswordValidatorMockedStatic = mockStatic(RepeatPasswordValidator.class);
             MockedStatic<PasswordValidator> passwordValidatorMockedStatic = mockStatic(PasswordValidator.class)) {
            repeatPasswordValidatorMockedStatic.when(() -> RepeatPasswordValidator
                            .checkRepeatPasswordSignUpUser(any(SaveUserDto.class)))
                    .thenAnswer(invocation -> null);
            passwordValidatorMockedStatic.when(() -> PasswordValidator.isSimple(any(String.class)))
                    .thenAnswer(invocation -> null);
            mockMvc.perform(post("/sign/up"))
                    .andExpect(model().attributeExists("passwordError", "saveUserDto"))
                    .andExpect(view().name("sign-up/sign-up-with-errors"));
        }
        verify(userService, times(1)).save(any(User.class));
    }

    @Test
    void whenSignUpUserAndPasswordIsSmallThenThrowException() throws Exception {
        when(userService.save(any(User.class))).thenThrow(SmallLengthPasswordException.class);
        try (MockedStatic<RepeatPasswordValidator> repeatPasswordValidatorMockedStatic = mockStatic(RepeatPasswordValidator.class);
             MockedStatic<PasswordValidator> passwordValidatorMockedStatic = mockStatic(PasswordValidator.class)) {
            repeatPasswordValidatorMockedStatic.when(() -> RepeatPasswordValidator
                            .checkRepeatPasswordSignUpUser(any(SaveUserDto.class)))
                    .thenAnswer(invocation -> null);
            passwordValidatorMockedStatic.when(() -> PasswordValidator.isSimple(any(String.class)))
                    .thenAnswer(invocation -> null);
            mockMvc.perform(post("/sign/up"))
                    .andExpect(model().attributeExists("passwordError", "saveUserDto"))
                    .andExpect(view().name("sign-up/sign-up-with-errors"));
        }
        verify(userService, times(1)).save(any(User.class));
    }

    @Test
    void whenSignUpUserAndLoginAlreadyExistsThenThrowException() throws Exception {
        when(userService.save(any(User.class))).thenThrow(UserAlreadyExistsException.class);
        try (MockedStatic<RepeatPasswordValidator> repeatPasswordValidatorMockedStatic = mockStatic(RepeatPasswordValidator.class);
             MockedStatic<PasswordValidator> passwordValidatorMockedStatic = mockStatic(PasswordValidator.class)) {
            repeatPasswordValidatorMockedStatic.when(() -> RepeatPasswordValidator
                            .checkRepeatPasswordSignUpUser(any(SaveUserDto.class)))
                    .thenAnswer(invocation -> null);
            passwordValidatorMockedStatic.when(() -> PasswordValidator.isSimple(any(String.class)))
                    .thenAnswer(invocation -> null);
            mockMvc.perform(post("/sign/up"))
                    .andExpect(model().attributeExists("usernameError", "saveUserDto"))
                    .andExpect(view().name("sign-up/sign-up-with-errors"));
        }
        verify(userService, times(1)).save(any(User.class));
    }

    @Test
    void whenSignUpThenOk() throws Exception {
        when(userService.save(any(User.class))).thenReturn(Optional.of(new User()));
        try (MockedStatic<RepeatPasswordValidator> repeatPasswordValidatorMockedStatic = mockStatic(RepeatPasswordValidator.class);
             MockedStatic<PasswordValidator> passwordValidatorMockedStatic = mockStatic(PasswordValidator.class)) {
            repeatPasswordValidatorMockedStatic.when(() -> RepeatPasswordValidator
                    .checkRepeatPasswordSignUpUser(any(SaveUserDto.class)))
                    .thenAnswer(invocation -> null);
            passwordValidatorMockedStatic.when(() -> PasswordValidator.isSimple(any(String.class)))
                    .thenAnswer(invocation -> null);
            mockMvc.perform(post("/sign/up")
                            .param("login", "testUser")
                            .param("password", "password123")
                            .param("repeatPassword", "password123"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/sign/in"));
        }
        verify(userService, times(1)).save(any(User.class));
    }


    @Test
    void whenPostSignInThenOk() throws Exception {
        User user = new User();
        user.setLogin("login");
        when(userService.getUserByLogin(any(SignInUserDto.class))).thenReturn(Optional.of(user));
        doNothing().when(sessionService).authenticateAndManageSession(eq(user), any(HttpServletResponse.class));
        mockMvc.perform(post("/sign/in")
                        .param("login", "login")
                        .param("password", "123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/index"));
        verify(sessionService, times(1)).authenticateAndManageSession(eq(user), any(HttpServletResponse.class));
    }

    @Test
    void whenPostSignInAndUserNotFoundThenThrowException() throws Exception {
        when(userService.getUserByLogin(any(SignInUserDto.class)))
                .thenThrow(new UserNotFoundException("User was not found"));
        mockMvc.perform(post("/sign/in")
                        .param("login", "nonexistent")
                        .param("password", "123"))
                .andExpect(status().isOk())
                .andExpect(view().name("sign-in/sign-in-with-errors"))
                .andExpect(model().attributeExists("usernameError"))
                .andExpect(model().attributeExists("signInUserDto"));
        verify(sessionService, never()).authenticateAndManageSession(any(), any());
    }

    @Test
    void whenInputPasswordNotCorrectThenThrowIncorrectPasswordException() throws Exception {
        when(userService.getUserByLogin(any(SignInUserDto.class)))
                .thenThrow(new IncorrectPasswordException("Incorrect input password"));
        mockMvc.perform(post("/sign/in")
                        .param("login", "test_login")
                        .param("password", "111"))
                .andExpect(status().isOk())
                .andExpect(view().name("sign-in/sign-in-with-errors"))
                .andExpect(model().attributeExists("passwordError"));
        verify(sessionService, never()).authenticateAndManageSession(any(), any());
    }

    @Test
    void whenUnknownExceptionThenThrowRuntimeException() throws Exception {
        when(userService.getUserByLogin(any(SignInUserDto.class)))
                .thenThrow(new RuntimeException("Unknown exception"));
        mockMvc.perform(post("/sign/in")
                        .param("login", "test_login")
                        .param("password", "111"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/error"))
                .andExpect(model().attributeExists("error"));
        verify(sessionService, never()).authenticateAndManageSession(any(), any());
    }

    @Test
    void whenGetSignInPageThenOk() throws Exception {
        mockMvc.perform(get("/sign/in"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("signInUserDto"))
                .andExpect(model().attribute("signInUserDto", new SignInUserDto("", "")));
    }

    @Test
    void whenGetSignUpPageThenOk() throws Exception {
        mockMvc.perform(get("/sign/up"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("saveUserDto"))
                .andExpect(model().attribute("saveUserDto", new SaveUserDto("", "", "")));
    }

    @Test
    void whenPostSignOutUserAndSessionIsPresentThenRedirectSignIn() throws Exception {
        try (MockedStatic<SessionFinder> sessionFinderMocked = mockStatic(SessionFinder.class);
             MockedStatic<SessionDestroyer> sessionDestroyerMocked = mockStatic(SessionDestroyer.class)) {
            User testUser = new User();
            testUser.setLogin("test_user");
            Session session = new Session();
            session.setUser(testUser);
            session.setExpiresAt(LocalDateTime.now().plusHours(1));
            sessionFinderMocked.when(() -> SessionFinder.findSession(any(), any(), any()))
                    .thenReturn(Optional.of(session));
            sessionDestroyerMocked.when(() -> SessionDestroyer.invalidateSession(any(), any(), any()))
                    .thenAnswer(invocation -> null);
            mockMvc.perform(post("/sign/out"))
                    .andExpect(status().is3xxRedirection());
            sessionFinderMocked.verify(() -> SessionFinder.findSession(any(), any(), any()), times(1));
            sessionDestroyerMocked.verify(() -> SessionDestroyer.invalidateSession(any(), any(), any()), times(1));
        }
    }

    @Test
    void whenPostSignOutUserAndSessionIsNotPresentThenRedirectSignIn() throws Exception {
        try (MockedStatic<SessionFinder> sessionFinderMocked = mockStatic(SessionFinder.class)) {
            sessionFinderMocked.when(() -> SessionFinder.findSession(any(), any(), any()))
                    .thenReturn(Optional.empty());
            mockMvc.perform(post("/sign/out"))
                    .andExpect(status().is3xxRedirection());
            sessionFinderMocked.verify(() -> SessionFinder.findSession(any(), any(), any()), times(1));
        }
    }

    @Test
    void whenPostSignOutUserAndUserNotFoundThenShowErrorPage() throws Exception {
        UserNotFoundException exception = new UserNotFoundException("User not found");
        try (MockedStatic<SessionFinder> sessionFinderMocked = mockStatic(SessionFinder.class)) {
            sessionFinderMocked.when(() -> SessionFinder.findSession(any(), any(), any()))
                    .thenThrow(exception);
            mockMvc.perform(post("/sign/out"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("error/error"))
                    .andExpect(model().attributeExists("error"))
                    .andExpect(model().attribute("error", "User not found"));
           sessionFinderMocked.verify(() -> SessionFinder.findSession(any(), any(), any()), times(1));
        }
    }

}