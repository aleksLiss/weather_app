package com.weather.app.controller;

import com.weather.app.dto.FoundLocationDto;
import com.weather.app.dto.SearchLocationDto;
import com.weather.app.dto.WeatherDto;
import com.weather.app.exception.CookiesNotFoundException;
import com.weather.app.exception.session.SessionNotFoundException;
import com.weather.app.exception.username.UserNotFoundException;
import com.weather.app.model.Session;
import com.weather.app.model.User;
import com.weather.app.service.LocationService;
import com.weather.app.service.SessionService;
import com.weather.app.service.WeatherApiService;
import com.weather.app.util.session.SessionFinder;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/location")
public class LocationController {

    private final LocationService locationService;
    private final WeatherApiService weatherApiService;
    private final SessionService sessionService;
    private final Environment environment;
    private static final Logger LOGGER = LoggerFactory.getLogger(LocationController.class);

    public LocationController(LocationService locationService, WeatherApiService weatherApiService, SessionService sessionService, Environment environment) {
        this.locationService = locationService;
        this.weatherApiService = weatherApiService;
        this.sessionService = sessionService;
        this.environment = environment;
    }

    @GetMapping("/search")
    public String getSearchPage(Model model,
                                HttpServletRequest request) {
        try {
            User user = getAuthenticateUser(request);
            List<WeatherDto> weatherDtos = weatherApiService.getWeatherForUser(user);
            model.addAttribute("login", user.getLogin());
            model.addAttribute("weatherDtoList", weatherDtos);
            model.addAttribute("location", new SearchLocationDto(""));
            model.addAttribute("foundLocationDto", new FoundLocationDto());
        } catch (Exception ex) {
            LOGGER.warn("Error get search page");
            model.addAttribute("error", ex.getMessage());
            return "error/error";
        }
        return "search/search";
    }

    @PostMapping("/search")
    public String searchLocation(@ModelAttribute("location") SearchLocationDto searchLocationDto,
                                 Model model,
                                 HttpServletRequest request) {
        try {
            User user = getAuthenticateUser(request);
            List<FoundLocationDto> foundLocationDtoList = weatherApiService
                    .getFoundLocationDtos(searchLocationDto.name());
            model.addAttribute("login", user.getLogin());
            model.addAttribute("locationsList", foundLocationDtoList);
            model.addAttribute("foundLocationDto", new FoundLocationDto());
        } catch (Exception ex) {
            LOGGER.warn("Error post search pages");
            model.addAttribute("error", ex.getMessage());
            return "error/error";
        }
        return "search/search";
    }

    @PostMapping("/add")
    public String saveLocation(@ModelAttribute("foundLocationDto") FoundLocationDto dto,
                               Model model,
                               HttpServletRequest request) {
        try {
            User user = getAuthenticateUser(request);
            locationService.save(dto, user);
            model.addAttribute("searchLocationDto", new SearchLocationDto(""));
        } catch (Exception ex) {
            LOGGER.warn("Location by this user already exists");
            return "redirect:/index";
        }
        return "redirect:/index";
    }

    @PostMapping("/delete")
    public String deleteLocation(@RequestParam("locationName") String location,
                                 HttpServletRequest request,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        try {
            User user = getAuthenticateUser(request);
            locationService.deleteLocationByLocationNameAndUserId(location, user.getId());
        } catch (Exception ex) {
            LOGGER.warn("Error delete location");
            model.addAttribute("error", ex.getMessage());
            redirectAttributes.addFlashAttribute("error", "Не удалось удалить локацию");
            return "error/error";
        }
        return "redirect:/index";
    }

    @ModelAttribute
    public void addCommonsAttributes(HttpServletRequest request,
                                     Model model) {
        try {
            SessionFinder.findSession(request, environment, sessionService)
                    .ifPresent(session -> model.addAttribute("login", session.getUser().getLogin()));
        } catch (CookiesNotFoundException | SessionNotFoundException e) {
            LOGGER.warn("session or cookie was not found");
        }
    }

    User getAuthenticateUser(HttpServletRequest request) {
        return SessionFinder.findSession(request, environment, sessionService)
                .map(Session::getUser)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
