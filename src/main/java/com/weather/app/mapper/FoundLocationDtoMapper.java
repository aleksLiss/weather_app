package com.weather.app.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.app.dto.FoundLocationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class FoundLocationDtoMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(FoundLocationDtoMapper.class);

    public List<FoundLocationDto> getFoundLocationDtoListFromStringJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json,
                    objectMapper.getTypeFactory().constructCollectionType(
                            List.class, FoundLocationDto.class
                    ));
        } catch (Exception ex) {
            LOGGER.warn("Failed to parse locations JSON: {}", ex.getMessage());
            return Collections.emptyList();
        }
    }

}
