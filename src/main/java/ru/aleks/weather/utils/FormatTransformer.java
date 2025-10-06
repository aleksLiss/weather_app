package ru.aleks.weather.utils;

import ru.aleks.weather.dto.LocationAnswerDto;

import java.util.Optional;

public interface FormatTransformer {

    Optional<LocationAnswerDto> transform(String json);
}
