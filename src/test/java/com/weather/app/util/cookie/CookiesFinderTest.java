package com.weather.app.util.cookie;

import com.weather.app.exception.CookiesNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CookiesFinderTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private Cookie cookie;

    @Test
    void whenGetCookiesThenReturnCookiesArray() {
        Cookie[] cookies = new Cookie[]{cookie};
        Mockito.when(request.getCookies()).thenReturn(cookies);
        Cookie[] actualCookies = CookiesFinder.getCookies(request);
        assertThat(actualCookies).isEqualTo(cookies);
    }

    @Test
    void whenCookiesNotFoundThenThrowException() {
        Mockito.when(request.getCookies()).thenReturn(null);
        assertThatThrownBy(() -> CookiesFinder.getCookies(request))
                .isInstanceOf(CookiesNotFoundException.class)
                .hasMessage("Cookies was not found");
        verify(request, times(1)).getCookies();
    }
}