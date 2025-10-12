package ru.aleks.weather.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.aleks.weather.dto.FoundWeatherDto;
import ru.aleks.weather.dto.LocationTransform;
import ru.aleks.weather.dto.LocationGetDto;
import ru.aleks.weather.service.LocationService;
import ru.aleks.weather.service.UserService;
import ru.aleks.weather.service.WeatherApiService;
import ru.aleks.weather.utils.CheckLocations;
import ru.aleks.weather.utils.TemperatureTransformer;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.aleks.weather.utils.Locations.CITY;
import static ru.aleks.weather.utils.Locations.COORDINATES;

@Controller
public class LocationController {

    private final LocationService locationService;
    private final UserService userService;
    private final WeatherApiService weatherApiService;
    private static final Logger LOGGER = LoggerFactory.getLogger(LocationController.class);
    private final TemperatureTransformer temperatureTransformer;
    private final CheckLocations checkCoordinates;

    public LocationController(LocationService locationService, UserService userService, WeatherApiService weatherApiService, TemperatureTransformer temperatureTransformer, CheckLocations checkCoordinates) {
        this.locationService = locationService;
        this.userService = userService;
        this.weatherApiService = weatherApiService;
        this.temperatureTransformer = temperatureTransformer;
        this.checkCoordinates = checkCoordinates;
    }

    @GetMapping("/location/city")
    public String getSearchLocationPage(Model model) {
        model.addAttribute("location", new LocationTransform());
        return "search-results";
    }

    @PostMapping("/location/city")
    public String getLocationFromApi(@ModelAttribute LocationGetDto locationGetDto,
                                     Model model) {
        String locationFromDto = locationGetDto.getLocation();
        if (checkCoordinates.checkLocationByRegex(CITY, locationFromDto)) {
            Optional<LocationTransform> answerDto = weatherApiService.getWeatherByCityName(locationFromDto);
            FoundWeatherDto foundWeatherDto2 = getFoundWeatherDto(answerDto.get());
            model.addAttribute("weather", foundWeatherDto2);
            return "search-results";
        }
        if (checkCoordinates.checkLocationByRegex(COORDINATES, locationFromDto)) {
            Pattern patt = Pattern.compile("^\\s*(\\d+(?:\\.\\d+)?)\\s*[ ,]\\s*(\\d+(?:\\.\\d+)?)\\s*$");
            Matcher matcher = patt.matcher(locationFromDto);
            if (!matcher.matches()) {
                LOGGER.warn("LocationController: incorrect coordinates");
                model.addAttribute("message", "incorrect coordinates");
                return "errors/error";
            }
            double latitude = Double.parseDouble(matcher.group(1));
            double longitude = Double.parseDouble(matcher.group(2));
            Optional<LocationTransform> locationTransform = weatherApiService.getWeatherByCoordinates(latitude, longitude);
            FoundWeatherDto foundWeatherDto = getFoundWeatherDto(locationTransform.get());
            model.addAttribute("weather", foundWeatherDto);
            return "search-results";
        }
        LOGGER.warn("LocationController: city or coordinates not correct");
        model.addAttribute("message", "city or coordinates not correct");
        return "errors/error";
        }

        // todo add new method for save found location into repository
        /*
        @PostMapping("/location/add")
        public String addLocationToUser() {

        }
         */

    private FoundWeatherDto getFoundWeatherDto(LocationTransform answerDto) {
        FoundWeatherDto foundWeatherDto = new FoundWeatherDto();
        foundWeatherDto.setCountry(
                answerDto.getCountry()
        );
        foundWeatherDto.setHumidity(
                answerDto.getHumidity()
        );
        foundWeatherDto.setDescription(
                answerDto.getDescription()
        );
        foundWeatherDto.setName(
                answerDto.getCity()
        );
        foundWeatherDto.setTemperature(
                String.format("%.1f",
                        temperatureTransformer.fromKelvinsToCelsius(
                                answerDto.getTemperature()
                        ))
        );
        return foundWeatherDto;
    }
}
