package ru.aleks.weather.dto.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.aleks.weather.dto.DtoSetter;
import ru.aleks.weather.dto.LocationSendDto;
import ru.aleks.weather.dto.LocationTransform;
import ru.aleks.weather.utils.TemperatureTransformer;

@Component
public class LocationSendDtoSetter implements DtoSetter<LocationSendDto> {

    private static final String BASE_URL = "https://openweathermap.org/img/wn/";
    private static final String IMG_EXTENSION = ".png";
    private final TemperatureTransformer temperatureTransformer;
    private static final Logger LOGGER = LoggerFactory.getLogger(LocationSendDtoSetter.class);
    public LocationSendDtoSetter(TemperatureTransformer temperatureTransformer) {
        this.temperatureTransformer = temperatureTransformer;
    }

    @Override
    public LocationSendDto setDto(LocationTransform locationTransform) {
        LocationSendDto locationSendDto = new LocationSendDto();
        String urlForImg = BASE_URL + locationTransform.getIcon() + IMG_EXTENSION;
        locationSendDto.setIcon(urlForImg);
        LOGGER.warn("LocationSendDtoSetter  :        " + locationTransform.getCity());
        locationSendDto.setCityName(locationTransform.getCity());
        locationSendDto.setTemperature(
                String.format("%.1f", temperatureTransformer.fromKelvinsToCelsius(locationTransform.getTemperature())));
        locationSendDto.setHumidity(locationTransform.getHumidity());
        locationSendDto.setCountryName(locationTransform.getCountry());
        locationSendDto.setDescriptionWeather(locationTransform.getDescription());
        return locationSendDto;
    }
}
