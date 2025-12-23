package com.weather.app.mapper;

import com.weather.app.dto.WeatherDto;
import com.weather.app.dto.WeatherResponseDto;
import com.weather.app.model.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class WeatherDtoMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherDtoMapper.class);

    public List<WeatherDto> map(Map<Location, WeatherResponseDto> weatherResponseDtoMap) {
        List<WeatherDto> result = new ArrayList<>();
        for (Map.Entry<Location, WeatherResponseDto> entry : weatherResponseDtoMap.entrySet()) {
            WeatherDto weatherDto = new WeatherDto();
            weatherDto.setLocation(entry.getKey().getName());
            weatherDto.setImage(entry.getValue().getImage());
            weatherDto.setDescription(entry.getValue().getDescription());
            weatherDto.setTemperature(entry.getValue().getTemperature());
            weatherDto.setHumidity(entry.getValue().getHumidity());
            result.add(weatherDto);
        }
        return result;
    }
}
