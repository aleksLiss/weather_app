package com.weather.app.service;

import com.weather.app.dto.SearchLocationDto;
import com.weather.app.dto.WeatherDto;
import com.weather.app.model.Session;
import com.weather.app.model.User;
import com.weather.app.util.session.SessionFinder;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IndexPageServiceTest {

    @Mock
    private WeatherApiService weatherApiService;
    @Mock
    private SessionService sessionService;
    @Mock
    private Environment environment;
    @Mock
    private Model model;
    @Mock
    private HttpServletRequest request;
    @InjectMocks
    private IndexPageService indexPageService;

    @Test
    void whenIsPopulateIndexModelThenReturnTrue() {
        User user = new User();
        user.setLogin("vova");
        Session session = mock(Session.class);
        when(session.getUser()).thenReturn(user);
        List<WeatherDto> weatherList = Collections.singletonList(new WeatherDto());
        when(weatherApiService.getWeatherForUser(user))
                .thenReturn(weatherList);
        try (MockedStatic<SessionFinder> finderMockedStatic = mockStatic(SessionFinder.class)) {

            finderMockedStatic.when(() -> SessionFinder.findSession(request, environment, sessionService))
                    .thenReturn(Optional.of(session));
            boolean result = indexPageService.isPopulateIndexModel(model, request);
            assertThat(result).isTrue();
            verify(model).addAttribute("login", user.getLogin());
            verify(model).addAttribute("searchLocationDto", new SearchLocationDto(""));
            verify(model).addAttribute("weatherDtoList", weatherList);
        }
    }

    @Test
    void whenIsNotPopulateIndexModelThenReturnTrue() {
        try (MockedStatic<SessionFinder> finderMockedStatic = mockStatic(SessionFinder.class)) {
            finderMockedStatic.when(() -> SessionFinder.findSession(request, environment, sessionService))
                    .thenReturn(Optional.empty());
            boolean result = indexPageService.isPopulateIndexModel(model, request);
            assertThat(result).isFalse();
            verifyNoInteractions(weatherApiService);
            verify(model, never()).addAttribute(anyString(), any());
        }
    }

}