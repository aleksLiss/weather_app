package com.weather.app.service;

import com.weather.app.dto.SearchLocationDto;
import com.weather.app.dto.WeatherDto;
import com.weather.app.model.User;
import com.weather.app.util.session.SessionFinder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class IndexPageService {

    private final WeatherApiService weatherApiService;
    private final SessionService sessionService;
    private final Environment environment;

    public IndexPageService(WeatherApiService weatherApiService, SessionService sessionService, Environment environment) {
        this.weatherApiService = weatherApiService;
        this.sessionService = sessionService;
        this.environment = environment;
    }

    public boolean isPopulateIndexModel(Model model, HttpServletRequest request) {
        return SessionFinder.findSession(request, environment, sessionService)
                .map(
                        session -> {
                            User user = session.getUser();
                            List<WeatherDto> weatherDtosByUser = weatherApiService.getWeatherForUser(user);
                            model.addAttribute("login", user.getLogin());
                            model.addAttribute("searchLocationDto", new SearchLocationDto(""));
                            model.addAttribute("weatherDtoList", weatherDtosByUser);
                            return true;
                        }
                ).orElse(false);
    }
}
