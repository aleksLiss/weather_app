package ru.aleks.weather.utils.impl;

import org.springframework.stereotype.Component;
import ru.aleks.weather.utils.TemperatureTransformer;

@Component
public class KelvinToCelsiusTransformer implements TemperatureTransformer {
    @Override
    public double fromKelvinsToCelsius(double temperature) {
        return temperature - 273.15;
    }
}
