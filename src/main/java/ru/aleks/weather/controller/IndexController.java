package ru.aleks.weather.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.aleks.weather.auth.AuthenticateUser;
import ru.aleks.weather.dto.*;
import ru.aleks.weather.exception.FailGetBodyFromResponseException;
import ru.aleks.weather.exception.FailedDeleteLocationException;
import ru.aleks.weather.exception.LocationNotFoundException;
import ru.aleks.weather.exception.UserNotFoundException;
import ru.aleks.weather.model.Location;
import ru.aleks.weather.model.User;
import ru.aleks.weather.service.LocationService;
import ru.aleks.weather.service.WeatherApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class IndexController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);
    private final LocationService locationService;
    private final WeatherApiService weatherApiService;
    private final AuthenticateUser authenticateUser;
    private final DtoSetter locationSendDtoDtoSetter;

    public IndexController(LocationService locationService, WeatherApiService weatherApiService, AuthenticateUser authenticateUser, DtoSetter locationSendDtoDtoSetter) {
        this.locationService = locationService;
        this.weatherApiService = weatherApiService;
        this.authenticateUser = authenticateUser;
        this.locationSendDtoDtoSetter = locationSendDtoDtoSetter;
    }

    @GetMapping("/")
    public String getIndex(HttpServletRequest request,
                           Model model) {
        Optional<User> foundUser;
        try {
            foundUser = authenticateUser.findUser(request, model);

            List<Location> locationList = locationService.getAllLocationsByUserId(foundUser.get().getId());
            List<LocationSendDto> locationSendDtos = new ArrayList<>();
            // todo readme!!!!!!!!!!!!!!!!!!!!!!11
            //  так как сохраняет с названием минск а подтягивает
            //  с апи с нназванием минск сити нужно название брать из репозитория
            //  а доп инфу из апи либо как то по другому делать я хуй знает я пиво пью
            // todo исправить ебучие тесты !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            for (Location location : locationList) {

                Optional<LocationTransform> locationTransform = weatherApiService.getWeatherByCoordinates(
                        location.getLatitude(),
                        location.getLongitude()
                );

                LocationSendDto created = (LocationSendDto) locationSendDtoDtoSetter.setDto(locationTransform.get());
                locationSendDtos.add(created);

            }
            model.addAttribute("locations", locationSendDtos);
            return "index";
        } catch (UserNotFoundException | LocationNotFoundException | FailGetBodyFromResponseException ex) {
            return createExceptionMessage(model, ex.getMessage());
        } catch (Exception ex) {
            return createExceptionMessage(model, "unknown message");
        }
    }

    @PostMapping("/delete")
    public String deleteLocation(@RequestParam("cityName") String cityName,
                                 HttpServletRequest request,
                                 Model model) {
        try {
            Optional<User> foundUser = authenticateUser.findUser(request, model);
            locationService.deleteLocationByUserId(foundUser.get().getId(), cityName);
        } catch (UserNotFoundException | FailedDeleteLocationException ex) {
            return createExceptionMessage(model, ex.getMessage());
        } catch (Exception ex) {
            return createExceptionMessage(model, "unknown exception");
        }
        return "redirect:/";
    }

    private String createExceptionMessage(Model model, String msg) {
        ExceptionDto exceptionDto = new ExceptionDto(msg);
        LOGGER.warn("IndexController: {}", exceptionDto.getMessage());
        model.addAttribute("exceptionDto", exceptionDto);
        return "errors/error";
    }
}