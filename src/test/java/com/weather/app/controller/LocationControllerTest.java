package com.weather.app.controller;

import com.weather.app.dto.FoundLocationDto;
import com.weather.app.dto.WeatherDto;
import com.weather.app.exception.username.UserAlreadyExistsException;
import com.weather.app.model.Session;
import com.weather.app.model.User;
import com.weather.app.service.LocationService;
import com.weather.app.service.SessionService;
import com.weather.app.service.WeatherApiService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class LocationControllerTest {

    @Mock
    private LocationService locationService;
    @Mock
    private WeatherApiService weatherApiService;
    @Mock
    private SessionService sessionService;
    @Mock
    private Environment environment;
    private MockMvc mockMvc;
    @Spy
    @InjectMocks
    private LocationController locationController;

    @BeforeEach
    void setUp() {
        locationService = mock(LocationService.class);
        weatherApiService = mock(WeatherApiService.class);
        LocationController controller = new LocationController(
                locationService,
                weatherApiService,
                sessionService,
                environment
        );
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void whenGetSearchPageThenOk() throws Exception {
        User user = new User();
        user.setLogin("login");
        List<WeatherDto> weatherDtos = List.of(
                new WeatherDto("gomel", "sun.png", 25.0, "Sunny", 50)
        );
        Session mockSession = new Session();
        mockSession.setUser(user);
        when(environment.getProperty("spring.application.session.id.name")).thenReturn("WEATHER_SESSION_ID");
        when(sessionService.getSessionByUUID(any())).thenReturn(Optional.of(mockSession));
        when(weatherApiService.getWeatherForUser(user)).thenReturn(weatherDtos);
        mockMvc.perform(get("/location/search")
                        .cookie(new Cookie("WEATHER_SESSION_ID", "some-uuid")))
                .andExpect(status().isOk())
                .andExpect(model().attribute("login", user.getLogin()))
                .andExpect(model().attribute("weatherDtoList", weatherDtos))
                .andExpect(model().attributeExists("location", "foundLocationDto"));
    }

    @Test
    void whenGetSearchPageThenThrowException() throws Exception {
        mockMvc.perform(get("/location/search"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/error"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void whenPostSearchLocationThenOk() throws Exception {
        User user = new User();
        user.setLogin("login");
        String city = "gomel";
        FoundLocationDto foundLocationDto = new FoundLocationDto();
        foundLocationDto.setName(city);
        Session mockSession = new Session();
        mockSession.setUser(user);
        List<FoundLocationDto> expectedFoundLocationDtos = List.of(foundLocationDto);
        when(environment.getProperty("spring.application.session.id.name")).thenReturn("WEATHER_SESSION_ID");
        when(sessionService.getSessionByUUID(any())).thenReturn(Optional.of(mockSession));
        when(weatherApiService.getFoundLocationDtos(city)).thenReturn(expectedFoundLocationDtos);
        List<FoundLocationDto> resultLocationsByCity = weatherApiService.getFoundLocationDtos(city);
        assertThat(resultLocationsByCity)
                .isNotEmpty()
                .hasSize(1)
                .containsExactlyInAnyOrder(foundLocationDto);
        mockMvc.perform(post("/location/search")
                        .cookie(new Cookie("WEATHER_SESSION_ID", "some-uuid"))
                        .param("name", city))
                .andExpect(model().attributeExists("login", "locationsList", "foundLocationDto"))
                .andExpect(model().attribute("login", user.getLogin()))
                .andExpect(model().attribute("locationsList", resultLocationsByCity))
                .andExpect(model().attribute("foundLocationDto", new FoundLocationDto()));
    }

    @Test
    void whenPostSearchPageThenThrowException() throws Exception {
        mockMvc.perform(post("/location/search"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/error"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void whenPostDeleteLocationThenOk() throws Exception {
        User user = new User();
        user.setLogin("123");
        user.setId(1);
        Session mockSession = new Session();
        mockSession.setUser(user);
        String locationToDelete = "gomel";
        when(environment.getProperty("spring.application.session.id.name")).thenReturn("WEATHER_SESSION_ID");
        when(sessionService.getSessionByUUID(any())).thenReturn(Optional.of(mockSession));
        mockMvc.perform(post("/location/delete")
                        .cookie(new Cookie("WEATHER_SESSION_ID", "random-uuid"))
                        .param("locationName", locationToDelete))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/index"));
        verify(locationService, times(1))
                .deleteLocationByLocationNameAndUserId(eq(locationToDelete), eq(user.getId()));
    }

    @Test
    void whenPostDeleteLocationThenThrowException() throws Exception {
        mockMvc.perform(post("/location/delete")
                        .param("locationName", "gomel"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/error"))
                .andExpect(model().attribute("error", "Cookies was not found"));
        verify(locationService, never()).deleteLocationByLocationNameAndUserId(eq("gomel"), anyInt());
    }

    @Test
    void whenSaveLocationThenOk() throws Exception {
        User user = new User();
        user.setLogin("login");
        String city = "gomel";
        FoundLocationDto dto = new FoundLocationDto();
        dto.setName(city);
        Session mockSession = new Session();
        mockSession.setUser(user);
        when(environment.getProperty("spring.application.session.id.name")).thenReturn("WEATHER_SESSION_ID");
        when(sessionService.getSessionByUUID(any())).thenReturn(Optional.of(mockSession));
        mockMvc.perform(post("/location/add")
                        .cookie(new Cookie("WEATHER_SESSION_ID", "some-uuid"))
                        .param("foundLocationDto", city))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/index"));
        verify(locationService, times(1)).save(any(FoundLocationDto.class), eq(user));
    }

    @Test
    void whenSaveLocationThenThrowException() throws Exception {
        User user = new User();
        user.setLogin("login");
        String city = "gomel";
        FoundLocationDto dto = new FoundLocationDto();
        dto.setName(city);
        Session mockSession = new Session();
        mockSession.setUser(user);
        when(environment.getProperty("spring.application.session.id.name")).thenReturn("WEATHER_SESSION_ID");
        when(sessionService.getSessionByUUID(any())).thenReturn(Optional.of(mockSession));
        doThrow(UserAlreadyExistsException.class)
                .when(locationService)
                .save(dto, user);
        mockMvc.perform(post("/location/add")
                        .cookie(new Cookie("WEATHER_SESSION_ID", "some-uuid"))
                        .param("foundLocationDto", city))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/index"));
        verify(locationService, times(1)).save(any(FoundLocationDto.class), eq(user));
    }
}