package ru.aleks.weather.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.aleks.weather.dto.FoundWeatherDto;
import ru.aleks.weather.dto.LocationTransform;
import ru.aleks.weather.dto.LocationGetDto;
import ru.aleks.weather.model.Location;
import ru.aleks.weather.model.User;
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
        return "search-results";
    }

    @PostMapping("/location/city")
    public String getLocationFromApi(@ModelAttribute LocationGetDto locationGetDto,
                                     Model model,
                                     HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String foundUserName = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("username")) {
                LOGGER.info("IndexController: cookie value with name 'username': " + cookie.getValue());
                foundUserName = cookie.getValue();
            }
        }
        if (foundUserName != null) {
            model.addAttribute("username", foundUserName);
        }
        String locationFromDto = locationGetDto.getLocation();
        if (checkCoordinates.checkLocationByRegex(CITY, locationFromDto)) {
            Optional<LocationTransform> answerDto = weatherApiService.getWeatherByCityName(locationFromDto);
            FoundWeatherDto foundWeatherDto = getFoundWeatherDto(answerDto.get());
            String urlForImg = "https://openweathermap.org/img/wn/" + answerDto.get().getIcon() + ".png";
            LOGGER.warn("icon location found : " + urlForImg);
            foundWeatherDto.setIcon(urlForImg);
            model.addAttribute("weather", foundWeatherDto);
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
            String urlForImg = "https://openweathermap.org/img/wn/" + locationTransform.get().getIcon() + ".png";
            LOGGER.warn("icon location found : " + urlForImg);
            foundWeatherDto.setIcon(urlForImg);
            model.addAttribute("weather", foundWeatherDto);
            return "search-results";
        }
        model.addAttribute("weather", new FoundWeatherDto());
        LOGGER.warn("LocationController: city or coordinates not correct");
        model.addAttribute("message", "city or coordinates not correct");
        return "errors/error";
    }

    @PostMapping("/location/add")
    public String addLocationToUser(@ModelAttribute("weather") FoundWeatherDto foundWeatherDto,
                                    HttpServletRequest request,
                                    Model model) {
        Cookie[] cookies = request.getCookies();
        String username = "";
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("username")) {
                username = cookie.getValue();
                break;
            }
        }
        Optional<User> foundUser = userService.getUserByLogin(username);
        if (foundUser.isEmpty()) {
            model.addAttribute("username", "Гость");
        }
        model.addAttribute("username", foundUser.get());
        int id = foundUser.get().getId();
        Location location = new Location();

        Optional<LocationTransform> locationTransform = weatherApiService.getWeatherByCityName(foundWeatherDto.getName());

        location.setName(foundWeatherDto.getName());
        location.setUserId(id);

        location.setLatitude(locationTransform.get().getCoordsDto().getLat());
        location.setLongitude(locationTransform.get().getCoordsDto().getLon());

        locationService.save(location);

        return "redirect:/";
    }

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
