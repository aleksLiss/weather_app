package com.weather.app.util.session;

import com.weather.app.model.Session;
import com.weather.app.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TimeSessionCheckerTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private SessionService sessionService;
    @Mock
    private Environment environment;
    @Mock
    private Session session;

    @Test
    void whenTimeSessionIsStillValid() {
        try (var sessionFinder = mockStatic(SessionFinder.class);
            var mockedValidator = mockStatic(SessionTimeValidator.class)) {
            sessionFinder.when(() -> SessionFinder.findSession(request, environment, sessionService))
                    .thenReturn(Optional.of(session));
            TimeSessionChecker.checkSessionTime(request, response, sessionService, environment);
            mockedValidator.verify(() ->SessionTimeValidator.checkValidSessionTime(
                    eq(request), eq(response), eq(environment), eq(session)), times(1));
        }
    }

    @Test
    void whenTimeSessionIsNotValid() {
        try (var sessionFinder = mockStatic(SessionFinder.class);
        var mockedValidator = mockStatic(SessionTimeValidator.class)) {
            sessionFinder.when(() -> SessionFinder.findSession(request, environment, sessionService))
                    .thenReturn(Optional.empty());
            TimeSessionChecker.checkSessionTime(request, response, sessionService, environment);
            mockedValidator.verify(() -> SessionTimeValidator.checkValidSessionTime(
                    any(),any(),any(),any()
            ), never());
        }
    }
}
