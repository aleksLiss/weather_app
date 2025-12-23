package com.weather.app.util.session;

import com.weather.app.model.Session;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionTimeValidatorTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private Environment environment;

    @Test
    void whenTimeSessionIsValid() {
        Session session = new Session();
        session.setExpiresAt(LocalDateTime.now().plusHours(1));
        try (var mockedDestroyer = mockStatic(SessionDestroyer.class);
             var mockedValidator = mockStatic(SessionTimeValidator.class, CALLS_REAL_METHODS)) {
            mockedValidator.when(() -> SessionTimeValidator.isSessionTimeStillValid(session))
                    .thenReturn(true);
            String result = SessionTimeValidator.checkValidSessionTime(request, response, environment, session);
            assertThat(result).isEqualTo("redirect:/sign/in");
            mockedDestroyer.verify(
                    () -> SessionDestroyer.invalidateSession(any(), any(), any()),
                    times(0)
            );
        }
    }

    @Test
    void whenTimeSessionIsNotValid() {
        Session session = new Session();
        session.setExpiresAt(LocalDateTime.now().minusMinutes(10));
        try (var mockedDestroyer = mockStatic(SessionDestroyer.class);
             var mockedValidator = mockStatic(SessionTimeValidator.class, CALLS_REAL_METHODS)) {
            mockedValidator.when(() -> SessionTimeValidator.isSessionTimeStillValid(session))
                    .thenReturn(false);
            String result = SessionTimeValidator.checkValidSessionTime(request, response, environment, session);
            assertThat(result).isEqualTo("redirect:/sign/in");
            mockedDestroyer.verify(
                    () -> SessionDestroyer.invalidateSession(request, response, environment),
                    times(1)
            );
        }

    }
}