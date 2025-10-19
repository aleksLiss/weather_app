package ru.aleks.weather.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.aleks.weather.dto.LocationSendDto;
import ru.aleks.weather.dto.LocationTransform;
import ru.aleks.weather.model.Location;
import ru.aleks.weather.model.User;
import ru.aleks.weather.service.LocationService;
import ru.aleks.weather.service.UserService;
import ru.aleks.weather.service.WeatherApiService;
import ru.aleks.weather.utils.TemperatureTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class IndexController {

    private static final String NAMESESSION = "WEATHERAPPSESSIONID";
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);
    private final LocationService locationService;
    private final UserService userService;
    private final WeatherApiService weatherApiService;
    private final TemperatureTransformer temperatureTransformer;

    public IndexController(LocationService locationService, UserService userService, WeatherApiService weatherApiService, TemperatureTransformer temperatureTransformer) {
        this.locationService = locationService;
        this.userService = userService;
        this.weatherApiService = weatherApiService;
        this.temperatureTransformer = temperatureTransformer;
    }

    @GetMapping("/")
    public String getIndex(HttpServletRequest request,
                           Model model) {

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
            Optional<User> foundUser = userService.getUserByLogin(foundUserName);

            List<Location> locationList = locationService.getAllLocationsByUserId(foundUser.get().getId());
            List<LocationSendDto> locationSendDtos = new ArrayList<>();
            for (Location location : locationList) {
                LocationSendDto locationSendDto = new LocationSendDto();
                Optional<LocationTransform> locationTransform = weatherApiService.getWeatherByCoordinates(
                        location.getLatitude(),
                        location.getLongitude()
                );
                String urlForImg = "https://openweathermap.org/img/wn/" + locationTransform.get().getIcon() + ".png";
                locationSendDto.setIcon(urlForImg);
                locationSendDto.setCityName(location.getName());
                locationSendDto.setTemperature(
                        String.format("%.1f", temperatureTransformer.fromKelvinsToCelsius(locationTransform.get().getTemperature())));
                locationSendDto.setHumidity(locationTransform.get().getHumidity());
                locationSendDto.setCountryName(locationTransform.get().getCountry());
                locationSendDto.setDescriptionWeather(locationTransform.get().getDescription());
                locationSendDtos.add(locationSendDto);
            }
            model.addAttribute("locations", locationSendDtos);
            return "index";
        }
        return "redirect:/user/up";
    }

    @PostMapping("/delete")
    public String deleteLocation(@RequestParam("cityName") String cityName,
                                 HttpServletRequest request,
                                 Model model) {
        Cookie[] cookies = request.getCookies();
        String foundUserName = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("username")) {
                LOGGER.info("IndexController: cookie value with name 'username': " + cookie.getValue());
                foundUserName = cookie.getValue();
            }
        }
        LOGGER.info("send dto loc : " + cityName);
        LOGGER.info("user" + foundUserName);
        if (foundUserName != null) {
            model.addAttribute("username", foundUserName);
            Optional<User> foundUser = userService.getUserByLogin(foundUserName);
            locationService.deleteLocationByUserId(foundUser.get().getId(), cityName);
        }
        return "redirect:/";
    }
}