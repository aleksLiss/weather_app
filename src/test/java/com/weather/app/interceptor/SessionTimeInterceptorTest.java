package com.weather.app.interceptor;

import com.weather.app.util.session.TimeSessionChecker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class SessionTimeInterceptorTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private Object handler;
    @InjectMocks
    private SessionTimeInterceptor interceptor;

    @Test
    void WhenT() throws Exception {
        try (MockedStatic<TimeSessionChecker> mocked = mockStatic(TimeSessionChecker.class)) {
            boolean result = interceptor.preHandle(request, response, handler);
            assertThat(result).isTrue();
            mocked.verify(() -> TimeSessionChecker.checkSessionTime(eq(request), eq(response), any(), any()));
        }
    }

    @Test
    void WhenTThrow() {
        try (MockedStatic<TimeSessionChecker> mocked = mockStatic(TimeSessionChecker.class)) {

            mocked.when(() -> TimeSessionChecker.checkSessionTime(
                            any(HttpServletRequest.class),
                            any(HttpServletResponse.class),
                            any(),
                            any()))
                    .thenThrow(new RuntimeException());
            assertThrows(RuntimeException.class, () -> {
                interceptor.preHandle(request, response, handler);
            });
    }
}}