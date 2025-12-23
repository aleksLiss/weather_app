package com.weather.app.mapper;

import com.weather.app.dto.*;
import com.weather.app.model.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class WeatherDtoMapperTest {

    private Location location;
    private WeatherResponseDto weatherResponseDto;
    private Map<Location, WeatherResponseDto> weatherResponseDtoMap;
    private WeatherDtoMapper weatherDtoMapper;

    @BeforeEach
    void setUp() {
        location = new Location();
        location.setName("gomel");
        weatherResponseDto = new WeatherResponseDto();
        weatherResponseDto.setLocation("Gomel");
        MainData mainData = new MainData();
        mainData.setTemperature(293.15);
        mainData.setHumidity(60);
        weatherResponseDto.setMainData(mainData);
        WeatherData weatherData = new WeatherData();
        weatherData.setDescription("clear sky");
        weatherData.setIcon("01d");
        weatherResponseDto.setWeatherData(new WeatherData[]{weatherData});
        CoordsDto coordsDto = new CoordsDto();
        coordsDto.setLatitude(52.43);
        coordsDto.setLongitude(31.02);
        weatherResponseDto.setCoordsDto(coordsDto);
        weatherResponseDtoMap = Map.of(location, weatherResponseDto);
        weatherDtoMapper = new WeatherDtoMapper();
    }

    @Test
    void whenWeatherResponseDtoMapIsNotEmptyThenReturnListWithWeatherDto() {
        WeatherDto weatherDto = new WeatherDto();
        weatherDto.setLocation(location.getName());
        weatherDto.setImage(weatherResponseDto.getImage());
        weatherDto.setDescription(weatherResponseDto.getDescription());
        weatherDto.setTemperature(weatherResponseDto.getTemperature());
        weatherDto.setHumidity(weatherResponseDto.getHumidity());
        List<WeatherDto> result = weatherDtoMapper.map(weatherResponseDtoMap);
        assertThat(result)
                .isNotEmpty()
                .hasSize(1)
                .containsExactlyInAnyOrder(weatherDto);
    }

    @Test
    void whenWeatherResponseDtoMapIsEmptyThenReturnEmptyList() {
        List<WeatherDto> result = weatherDtoMapper.map(Map.of());
        assertThat(result)
                .isEmpty();
    }
}