package com.weather.app.util.session;

import com.weather.app.util.cookie.CookiesFinder;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionDestroyerTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private Environment environment;

    @Test
    void whenSessionIdIsPresent() {
        String expectedName = "WEATHER_SESSION_ID";
        Cookie sessionCookie = new Cookie(expectedName, "some-value");
        Cookie[] cookiesArray = new Cookie[]{sessionCookie};
        try (var cookieFinder = mockStatic(CookiesFinder.class);
             var nameSessionIdFinder = mockStatic(NameSessionIdFinder.class)) {
            cookieFinder.when(() -> CookiesFinder.getCookies(request))
                    .thenReturn(cookiesArray);
            nameSessionIdFinder.when(() -> NameSessionIdFinder.getNameSessionId(environment))
                    .thenReturn(expectedName);
            SessionDestroyer.invalidateSession(request, response, environment);
            ArgumentCaptor<Cookie> argumentCaptor = ArgumentCaptor.forClass(Cookie.class);
            verify(response, times(1)).addCookie(argumentCaptor.capture());
            Cookie capturedCookie = argumentCaptor.getValue();
            assertThat(capturedCookie).isNotNull();
            assertThat(capturedCookie.getName()).isEqualTo(expectedName);
            assertThat(capturedCookie.getValue()).isEmpty();
            assertThat(capturedCookie.getPath()).isEqualTo("/");
            assertThat(capturedCookie.getMaxAge()).isZero();
        }
    }

    @Test
    void whenSessionIdIsNotPresent() {
        String expectedName = "WEATHER_SESSION_ID";
        Cookie sessionCookie = new Cookie(expectedName, "some-value");
        Cookie[] cookiesArray = new Cookie[]{sessionCookie};
        try (var cookieFinder = mockStatic(CookiesFinder.class);
             var nameSessionIdFinder = mockStatic(NameSessionIdFinder.class)) {
            cookieFinder.when(() -> CookiesFinder.getCookies(request))
                    .thenReturn(cookiesArray);
            nameSessionIdFinder.when(() -> NameSessionIdFinder.getNameSessionId(environment))
                    .thenReturn(null);
            assertThatThrownBy(() -> SessionDestroyer.invalidateSession(request, response, environment))
                    .isInstanceOf(NullPointerException.class);
            verify(response, never()).addCookie(sessionCookie);
        }
    }
}