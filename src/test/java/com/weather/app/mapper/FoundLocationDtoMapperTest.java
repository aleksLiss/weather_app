package com.weather.app.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.weather.app.dto.FoundLocationDto;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class FoundLocationDtoMapperTest {

    private final FoundLocationDtoMapper locationMapper = new FoundLocationDtoMapper();

    @Test
    void whenJsonIsNotEmptyThenGetFoundLocationDto() throws JsonProcessingException {
        String json = """
                [
                    {
                        "name": "London",
                        "lat": 51.5074,
                        "lon": -0.1278,
                        "country": "GB"
                    },
                    {
                        "name": "Paris",
                        "lat": 48.8566,
                        "lon": 2.3522,
                        "country": "FR"
                    }
                ]
                """;
        List<FoundLocationDto> result = locationMapper.getFoundLocationDtoListFromStringJson(json);
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("London");
        assertThat(result.get(1).getName()).isEqualTo("Paris");
    }

    @Test
    void whenJsonIsEmptyThenThenGetEmptyList() {
        String invalidJson = "{\"invalid\": \"data\"}";
        List<FoundLocationDto> result = locationMapper.getFoundLocationDtoListFromStringJson(invalidJson);
        assertThat(result).isEqualTo(List.of());
    }

    @Test
    void shouldReturnEmptyList_WhenInputIsEmpty() {
        assertThat(locationMapper.getFoundLocationDtoListFromStringJson(null)).isEmpty();
        assertThat(locationMapper.getFoundLocationDtoListFromStringJson("")).isEmpty();
    }
}