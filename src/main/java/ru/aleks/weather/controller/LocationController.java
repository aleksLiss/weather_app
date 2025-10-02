package ru.aleks.weather.controller;

import org.springframework.stereotype.Controller;
import ru.aleks.weather.service.LocationService;

@Controller
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

}
