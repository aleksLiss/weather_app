package ru.aleks.weather.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.aleks.weather.dto.LocationAnswerDto;
import ru.aleks.weather.dto.LocationGetDto;
import ru.aleks.weather.service.LocationService;
import ru.aleks.weather.service.UserService;
import ru.aleks.weather.service.WeatherApiService;
import ru.aleks.weather.utils.CheckCoordinates;
import ru.aleks.weather.utils.TemperatureTransformer;

import java.util.Optional;

@Controller
public class LocationController {

    private final LocationService locationService;
    private final UserService userService;
    private final WeatherApiService weatherApiService;
    private static final Logger LOGGER = LoggerFactory.getLogger(LocationController.class);
    private final TemperatureTransformer temperatureTransformer;
    private final CheckCoordinates checkCoordinates;

    public LocationController(LocationService locationService, UserService userService, WeatherApiService weatherApiService, TemperatureTransformer temperatureTransformer, CheckCoordinates checkCoordinates) {
        this.locationService = locationService;
        this.userService = userService;
        this.weatherApiService = weatherApiService;
        this.temperatureTransformer = temperatureTransformer;
        this.checkCoordinates = checkCoordinates;
    }

    @GetMapping("/location/city")
    public String getSearchLocationPage(Model model) {
        model.addAttribute("location", new LocationAnswerDto());
        return "search-results";
    }

    @PostMapping("/location/city")
    public String getLocationFromApi(@ModelAttribute LocationGetDto locationGetDto,
                                        Model model) {

        String locationFromDto = locationGetDto.getLocation();
        if (!checkCoordinates.checkCoordinates(locationFromDto)) {
            LOGGER.warn("LocationController: city or coordinates not correct");
            model.addAttribute("message", "city or coordinates not correct");
            return "errors/error";
        }
        if (locationGetDto.getLocation() != null) {
            try {
            Optional<LocationAnswerDto> answerDto = weatherApiService.getWeatherByName(locationGetDto.getLocation());
                if (answerDto.isPresent()) {
                    LocationAnswerDto locationAnswerDto = answerDto.get();
                    model.addAttribute("cc", locationAnswerDto.getCity());
                    model.addAttribute("temperature", String.format("%.1f", temperatureTransformer
                            .fromKelvinsToCelsius(
                                    locationAnswerDto.getTemperature())));
                    model.addAttribute("humidity", locationAnswerDto.getHumidity());
                    model.addAttribute("country", locationAnswerDto.getCountry());
                    model.addAttribute("description", locationAnswerDto.getDescription());
                }
            } catch (Exception ex) {
                model.addAttribute("message", "city not found");
                return "errors/error";
            }
        } else {
            try {
                Optional<LocationAnswerDto> answerDto = weatherApiService.getWeatherByLocation(52.427063, 30.987352);
                if (answerDto.isPresent()) {
                    LocationAnswerDto locationAnswerDto = answerDto.get();
                    model.addAttribute("cc", locationAnswerDto.getCity());
                    model.addAttribute("temperature", String.format("%.1f", temperatureTransformer
                            .fromKelvinsToCelsius(
                                    locationAnswerDto.getTemperature())));
                    model.addAttribute("humidity", locationAnswerDto.getHumidity());
                    model.addAttribute("country", locationAnswerDto.getCountry());
                    model.addAttribute("description", locationAnswerDto.getDescription());
                }
            } catch (Exception ex) {
                model.addAttribute("message", "city not found");
                return "errors/error";
            }
        }
        return "search-results";
    }
}
