package com.weather.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.app.config.WeatherApiConfig;
import com.weather.app.dto.FoundLocationDto;
import com.weather.app.dto.WeatherDto;
import com.weather.app.dto.WeatherResponseDto;
import com.weather.app.mapper.FoundLocationDtoMapper;
import com.weather.app.mapper.WeatherDtoMapper;
import com.weather.app.model.Location;
import com.weather.app.model.User;
import com.weather.app.util.UrlFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import static java.nio.charset.StandardCharsets.*;

@Service
public class WeatherApiService {

    private final WeatherApiConfig weatherApiConfig;
    private final HttpClient httpClient;
    private final WeatherDtoMapper weatherDtoMapper;
    private final FoundLocationDtoMapper foundLocationDtoMapper;
    private final LocationService locationService;
    private final ObjectMapper objectMapper;

    private static final String BASE_URL_FOR_GET_LIST_CITIES = "https://api.openweathermap.org/geo/1.0/direct?q=%s&limit=%s&appid=%s";
    private static final String BASE_URL_FOR_GET_ONE_CITY_WEATHER = "https://api.openweathermap.org/data/2.5/weather?lat=%.6f&lon=%.6f&appid=%s";
    private static final int LIMIT_FOUND_CITIES = 5;
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherApiService.class);

    public WeatherApiService(WeatherApiConfig weatherApiConfig, HttpClient httpClient, WeatherDtoMapper weatherDtoMapper, FoundLocationDtoMapper foundLocationDtoMapper, LocationService locationService, ObjectMapper objectMapper) {
        this.weatherApiConfig = weatherApiConfig;
        this.httpClient = httpClient;
        this.weatherDtoMapper = weatherDtoMapper;
        this.foundLocationDtoMapper = foundLocationDtoMapper;
        this.locationService = locationService;
        this.objectMapper = objectMapper;
    }

    public List<FoundLocationDto> getFoundLocationDtos(String city) {
        String jsonResponse = getListCitiesByName(city);
        return foundLocationDtoMapper.getFoundLocationDtoListFromStringJson(jsonResponse);
    }

    public List<WeatherDto> getWeatherForUser(User user) {
        List<Location> locations = locationService.getLocationByUser(user);
        Map<Location, WeatherResponseDto> weatherMap = getWeatherResponseDtoMap(locations);
        return weatherDtoMapper.map(weatherMap);
    }

    String getListCitiesByName(String city) {
        String token = getApiKey();
        HttpResponse<String> response;
        try {
            String encodedCity = URLEncoder.encode(city, UTF_8);
            String url = UrlFactory.getUrlForLocationList(BASE_URL_FOR_GET_LIST_CITIES, encodedCity, LIMIT_FOUND_CITIES, token);
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException ex) { // Ловим только ошибки сети/прерывания
            LOGGER.error("Failed to fetch city list for: {}", city, ex);
            throw new RuntimeException("Failed to fetch location data", ex);
        }
        if (response.statusCode() >= 400) {
            throw new RuntimeException("API Error: " + response.statusCode());
        }
        return response.body();
    }

    Map<Location, WeatherResponseDto> getWeatherResponseDtoMap(List<Location> locations) {
        Map<Location, WeatherResponseDto> result = new HashMap<>();
        for (Location location : locations) {
            getWeatherResponseDtoByLatitudeAndLongitude(location.getLatitude(), location.getLongitude())
                    .ifPresent(responseDto -> result.put(location, responseDto));
        }
        return result;
    }

    private Optional<WeatherResponseDto> getWeatherResponseDtoByLatitudeAndLongitude(double lat, double lon) {
        try {
            String jsonBody = getLocationByLatitudeAndLongitude(lat, lon);
            return Optional.of(objectMapper.readValue(jsonBody, WeatherResponseDto.class));
        } catch (Exception e) {
            LOGGER.error("Failed to parse weather for coordinates: lat={}, lon={}", lat, lon, e);
            return Optional.empty();
        }
    }

    private String getLocationByLatitudeAndLongitude(double latitude, double longitude) {
        String token = getApiKey();
        HttpResponse<String> response;
        try {
            String url = UrlFactory.getUrlForOneLocation(BASE_URL_FOR_GET_ONE_CITY_WEATHER, latitude, longitude, token);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new RuntimeException("Failed to fetch weather data for coordinates", ex);
        }
        if (response.statusCode() >= 400) {
            throw new RuntimeException("API Error: " + response.statusCode());
        }
        return response.body();
    }

    private String getApiKey() {
        return weatherApiConfig.getToken();
    }
}
