package com.weather.app.util.cookie;

import com.weather.app.model.Session;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CookieFactoryTest {

    @Mock
    private Session session;
    @Mock
    private HttpServletResponse response;
    private String nameSessionId;

    @Test
    void whenSessionIsPresentThenCreatedCookieWithSessionId() throws Exception {
        nameSessionId = "WEATHER_SESSION_ID_TEST";
        session = new Session();
        session.setId(UUID.randomUUID());
        ArgumentCaptor<Cookie> cookieArgumentCaptor = ArgumentCaptor.forClass(Cookie.class);
        CookieFactory.createAndAddToResponseCookie(nameSessionId, session, response);
        verify(response, times(1)).addCookie(cookieArgumentCaptor.capture());
        Cookie capturedCookie = cookieArgumentCaptor.getValue();
        assertThat(capturedCookie).isNotNull();
        assertThat(nameSessionId).isEqualTo(capturedCookie.getName());
        assertThat(session.getId().toString()).isEqualTo(capturedCookie.getValue());
        assertThat(capturedCookie.getPath()).isEqualTo("/");
        assertThat(capturedCookie.isHttpOnly()).isTrue();
        assertThat(capturedCookie.getMaxAge()).isEqualTo(3600);
    }

    @Test
    void whenSessionIsPresentThenCreatedCookieWithSessionIdEqualsNull() throws Exception {
        nameSessionId = "WEATHER_SESSION_ID_TEST";
        session = new Session();
        ArgumentCaptor<Cookie> cookieArgumentCaptor = ArgumentCaptor.forClass(Cookie.class);
        CookieFactory.createAndAddToResponseCookie(nameSessionId, session, response);
        verify(response, times(1)).addCookie(cookieArgumentCaptor.capture());
        Cookie capturedCookie = cookieArgumentCaptor.getValue();
        assertThat(capturedCookie).isNotNull();
        assertThat(capturedCookie.getName()).isEqualTo(nameSessionId);
        assertThat(capturedCookie.getValue()).isEqualTo("null");
        assertThat(capturedCookie.getPath()).isEqualTo("/");
        assertThat(capturedCookie.isHttpOnly()).isTrue();
        assertThat(capturedCookie.getMaxAge()).isEqualTo(3600);
    }
}