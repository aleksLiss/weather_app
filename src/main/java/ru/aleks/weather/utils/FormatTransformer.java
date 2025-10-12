package ru.aleks.weather.utils;

import ru.aleks.weather.dto.LocationTransform;

import java.util.Optional;

public interface FormatTransformer {

    Optional<LocationTransform> transform(String json);
}
