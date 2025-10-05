package ru.aleks.weather.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.aleks.weather.dto.LocationAnswerDto;

public class JsonTransformer {

    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper object = new ObjectMapper();
        String json = """
                {
                    "coord": {
                        "lon": 27.5667,
                        "lat": 53.9
                    },
                    "weather": [
                        {
                            "id": 500,
                            "main": "Rain",
                            "description": "light rain",
                            "icon": "10d"
                        }
                    ],
                    "base": "stations",
                    "main": {
                        "temp": 284.69,
                        "feels_like": 283.83,
                        "temp_min": 284.69,
                        "temp_max": 284.69,
                        "pressure": 1006,
                        "humidity": 74,
                        "sea_level": 1006,
                        "grnd_level": 980
                    },
                    "visibility": 10000,
                    "wind": {
                        "speed": 5.18,
                        "deg": 234,
                        "gust": 5.84
                    },
                    "rain": {
                        "1h": 0.24
                    },
                    "clouds": {
                        "all": 89
                    },
                    "dt": 1759665205,
                    "sys": {
                        "country": "BY",
                        "sunrise": 1759637917,
                        "sunset": 1759678646
                    },
                    "timezone": 10800,
                    "id": 625144,
                    "name": "Minsk",
                    "cod": 200
                }
                """;
        object.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        LocationAnswerDto dto = object.readValue(json, LocationAnswerDto.class);

        System.out.println("Город: " + dto.getCity());
        System.out.println("Страна: " + dto.getCountry());
        System.out.println("Температура: " + dto.getTemperature());
        System.out.println("Погода: " + dto.getWeather());
        System.out.println("Описание: " + dto.getDescription());
        System.out.println("Влажность: " + dto.getHumidity());
    }
}
