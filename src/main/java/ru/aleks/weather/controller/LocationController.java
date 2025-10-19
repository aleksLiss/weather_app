package ru.aleks.weather.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.aleks.weather.auth.AuthenticateUser;
import ru.aleks.weather.dto.*;
import ru.aleks.weather.exception.*;
import ru.aleks.weather.model.Location;
import ru.aleks.weather.model.User;
import ru.aleks.weather.service.LocationService;
import ru.aleks.weather.service.WeatherApiService;
import ru.aleks.weather.utils.CheckLocations;
import ru.aleks.weather.utils.GetCoordinatesFromString;

import java.util.Optional;

import static ru.aleks.weather.utils.Locations.CITY;
import static ru.aleks.weather.utils.Locations.COORDINATES;

@Controller
public class LocationController {

    private final LocationService locationService;
    private final WeatherApiService weatherApiService;
    private static final Logger LOGGER = LoggerFactory.getLogger(LocationController.class);
    private final CheckLocations checkCoordinates;
    private final AuthenticateUser authenticateUser;
    private final DtoSetter<LocationSendDto> locationSendDtoDtoSetter;
    private final GetCoordinatesFromString getCoordinatesFromString;
    private final ModelSetter<Location> locationModelSetter;

    public LocationController(LocationService locationService, WeatherApiService weatherApiService, CheckLocations checkCoordinates, AuthenticateUser authenticateUser, DtoSetter<LocationSendDto> locationSendDtoDtoSetter, GetCoordinatesFromString getCoordinatesFromString, ModelSetter<Location> locationModelSetter) {
        this.locationService = locationService;
        this.weatherApiService = weatherApiService;
        this.checkCoordinates = checkCoordinates;
        this.authenticateUser = authenticateUser;
        this.locationSendDtoDtoSetter = locationSendDtoDtoSetter;
        this.getCoordinatesFromString = getCoordinatesFromString;
        this.locationModelSetter = locationModelSetter;
    }

    @GetMapping("/location/city")
    public String getSearchLocationPage(Model model) {
        return "search-results";
    }

    @PostMapping("/location/city")
    public String getLocationFromApi(@ModelAttribute LocationGetDto locationGetDto,
                                     Model model,
                                     HttpServletRequest request) {
        try {
            authenticateUser.findUser(request, model);
        } catch (UserNotFoundException ex) {
            return createExceptionMessage(model, ex.getMessage());
        } catch (Exception ex) {
            return createExceptionMessage(model, ex.getMessage());
        }
        String locationFromDto = locationGetDto.getLocation();
        try {
            if (checkCoordinates.checkLocationByRegex(CITY, locationFromDto)) {
                Optional<LocationTransform> locationTransform = weatherApiService.getWeatherByCityName(locationFromDto);
                LocationSendDto locationSendDto = locationSendDtoDtoSetter.setDto(locationTransform.get());
                model.addAttribute("locationSendDto", locationSendDto);
                return "search-results";
            }
            if (checkCoordinates.checkLocationByRegex(COORDINATES, locationFromDto)) {
                double latitude = getCoordinatesFromString.getCoordinates(locationFromDto)[0];
                double longitude = getCoordinatesFromString.getCoordinates(locationFromDto)[1];
                Optional<LocationTransform> locationTransform = weatherApiService.getWeatherByCoordinates(latitude, longitude);
                LocationSendDto locationSendDto = locationSendDtoDtoSetter.setDto(locationTransform.get());
                model.addAttribute("locationSendDto", locationSendDto);
                return "search-results";
            }
        } catch (LocationNotFoundException | FailedTransformJsonToDto ex) {
            return createExceptionMessage(model, ex.getMessage());
        } catch (Exception ex) {
            return createExceptionMessage(model, ex.getMessage());
        }
        return createExceptionMessage(model, "exception unknown");
    }

    @PostMapping("/location/add")
    public String addLocationToUser(@ModelAttribute LocationSendDto locationSendDto,
                                    HttpServletRequest request,
                                    Model model) {
        Optional<User> foundUser;
        int userId;
        try {
            foundUser = authenticateUser.findUser(request, model);
            userId = foundUser.get().getId();
        } catch (UserNotFoundException ex) {
            return createExceptionMessage(model, ex.getMessage());
        }
        String locationFromDto = locationSendDto.getCityName();
        try {
            Optional<LocationTransform> locationTransform = weatherApiService.getWeatherByCityName(locationFromDto);
            LOGGER.warn("city name from trans   " + locationTransform.get().getCity());
            Location location = locationModelSetter.modelSet(locationTransform.get(), userId);
            locationService.save(location);
            return "redirect:/";
        } catch (LocationNotFoundException | FailedTransformJsonToDto ex) {
            return createExceptionMessage(model, ex.getMessage());
        } catch (Exception ex) {
            return createExceptionMessage(model, ex.getMessage());
        }
    }

    private String createExceptionMessage(Model model, String msg) {
        ExceptionDto exceptionDto = new ExceptionDto(msg);
        LOGGER.warn("IndexController: {}", exceptionDto.getMessage());
        model.addAttribute("exceptionDto", exceptionDto);
        return "errors/error";
    }
}