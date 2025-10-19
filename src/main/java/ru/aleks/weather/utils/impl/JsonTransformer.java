package ru.aleks.weather.utils.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.aleks.weather.dto.LocationTransform;
import ru.aleks.weather.exception.FailedTransformJsonToDto;
import ru.aleks.weather.utils.FormatTransformer;

import java.util.Optional;

@Component
public class JsonTransformer implements FormatTransformer {

    private final ObjectMapper object;
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonTransformer.class);

    public JsonTransformer(ObjectMapper object) {
        this.object = object;
    }

    @Override
    public Optional<LocationTransform> transform(String json) {
        LocationTransform locationAnswerDto;
        object.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            locationAnswerDto = object.readValue(json, LocationTransform.class);
        } catch (Exception ex) {
            LOGGER.warn("JsonTransformer: error transform from json to LocationAnswerDto");
            throw new FailedTransformJsonToDto("error transform from json to LocationAnswerDto");
        }
        return Optional.of(locationAnswerDto);
    }
}
