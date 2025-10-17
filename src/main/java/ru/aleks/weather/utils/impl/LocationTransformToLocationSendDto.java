package ru.aleks.weather.utils.impl;

import org.springframework.stereotype.Component;
import ru.aleks.weather.dto.LocationSendDto;
import ru.aleks.weather.model.Location;
import ru.aleks.weather.service.WeatherApiService;
import ru.aleks.weather.utils.FormatTransformer;
import ru.aleks.weather.utils.ObjectsTransformer;

@Component
public class LocationTransformToLocationSendDto implements ObjectsTransformer {

    private final WeatherApiService weatherApiService;
    private final FormatTransformer formatTransformer;

    public LocationTransformToLocationSendDto(WeatherApiService weatherApiService, FormatTransformer formatTransformer) {
        this.weatherApiService = weatherApiService;
        this.formatTransformer = formatTransformer;
    }

    @Override
    public LocationSendDto transformTo(Location location) {
        weatherApiService.getWeatherByCityName(location.getName());
//        formatTransformer.transform()
        return null;
    }
}
