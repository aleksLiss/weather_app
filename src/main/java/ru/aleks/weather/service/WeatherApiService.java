package ru.aleks.weather.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.aleks.weather.config.TokenConfig;
import ru.aleks.weather.dto.LocationAnswerDto;
import ru.aleks.weather.utils.FormatTransformer;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.SECONDS;

@Service
public class WeatherApiService {

    private final TokenConfig tokenConfig;
    private final FormatTransformer jsonToDtoTransformer;

    private static final String URL = "https://api.openweathermap.org/data/2.5/weather?";
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherApiService.class);

    public WeatherApiService(TokenConfig tokenConfig, FormatTransformer jsonToDtoTransformer) {
        this.tokenConfig = tokenConfig;
        this.jsonToDtoTransformer = jsonToDtoTransformer;
    }


    public Optional<LocationAnswerDto> getWeatherByName(String city) throws URISyntaxException {
        Optional<LocationAnswerDto> locationAnswerDto;
        String cityUrl = URL + "q=%s&appid=%s";
        String url = String.format(
                cityUrl,
                city,
                tokenConfig.getToken());
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .timeout(Duration.of(10, SECONDS))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            locationAnswerDto = jsonToDtoTransformer.transform(response.body());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return locationAnswerDto;
    }

    public Optional<LocationAnswerDto> getWeatherByLocation(double latitude, double longitude) throws URISyntaxException {
        Optional<LocationAnswerDto> locationAnswerDto;
        String coordUrl = URL + "lat=%.6f&lon=%.6f&appid=%s";
        String url = String.format(
                coordUrl,
                latitude,
                longitude,
                tokenConfig.getToken());
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .timeout(Duration.of(10, SECONDS))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            locationAnswerDto = jsonToDtoTransformer.transform(response.body());
        } catch (IOException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        return locationAnswerDto;
    }
}