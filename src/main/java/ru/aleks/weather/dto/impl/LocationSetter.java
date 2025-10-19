package ru.aleks.weather.dto.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.aleks.weather.dto.LocationTransform;
import ru.aleks.weather.dto.ModelSetter;
import ru.aleks.weather.model.Location;
import ru.aleks.weather.service.WeatherApiService;

import java.util.Optional;

@Component
public class LocationSetter implements ModelSetter<Location> {

    private final WeatherApiService weatherApiService;
    private static final Logger LOGGER = LoggerFactory.getLogger(LocationSetter.class);

    public LocationSetter(WeatherApiService weatherApiService) {
        this.weatherApiService = weatherApiService;
    }

    @Override
    public Location modelSet(LocationTransform locationTransform, int userId) {
        Location location = new Location();
        location.setName(locationTransform.getCity());
        location.setUserId(userId);
        location.setLatitude(locationTransform.getCoordsDto().getLat());
        location.setLongitude(locationTransform.getCoordsDto().getLon());
        return location;
    }
}
