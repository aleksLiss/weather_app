package ru.aleks.weather.controller;

import org.springframework.expression.spel.ast.OpAnd;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.aleks.weather.dto.LocationAnswerDto;
import ru.aleks.weather.dto.LocationCityNameDto;
import ru.aleks.weather.dto.LocationCoordinatesDto;
import ru.aleks.weather.model.Location;
import ru.aleks.weather.service.LocationService;
import ru.aleks.weather.service.UserService;
import ru.aleks.weather.service.WeatherApiService;

import java.util.Optional;

@Controller
public class LocationController {

    private final LocationService locationService;
    private final UserService userService;
    private final WeatherApiService weatherApiService;

    public LocationController(LocationService locationService, UserService userService, WeatherApiService weatherApiService) {
        this.locationService = locationService;
        this.userService = userService;
        this.weatherApiService = weatherApiService;
    }

    @GetMapping("/location/city")
    public String getSearchLocationPage(@ModelAttribute LocationAnswerDto locationAnswerDto, Model model) {
        model.addAttribute("location", new LocationAnswerDto());
        return "search-results";
    }

    @PostMapping("/location/city")
    public String getLocationByCityName(@ModelAttribute LocationCityNameDto locationCityNameDto,
                                                   Model model) {
        System.out.println(locationCityNameDto.getCity());
        try {
            Optional<LocationAnswerDto> answerDto = weatherApiService.getWeatherByName(locationCityNameDto.getCity());
            System.out.println(answerDto.get().getCity());
            model.addAttribute("city", "minsk");
            model.addAttribute("temperature", "25");
            model.addAttribute("humidity", "70");
            model.addAttribute("country", "BY");
            model.addAttribute("description", "descrip");
        } catch (Exception ex) {
            model.addAttribute("message", "city not found");
            return "errors/404";
        }
        return "search-results";
    }

    @PostMapping("/coordinates")
    public String getLocationByCoordinates(@ModelAttribute LocationCoordinatesDto locationCoordinatesDto,
                                                      Model model) {
        // check: latitude = from -90 to 90, longitude = from -180 to 180
        return "";
    }
}
