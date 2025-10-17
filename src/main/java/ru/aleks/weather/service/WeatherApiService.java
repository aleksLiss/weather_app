package ru.aleks.weather.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.aleks.weather.config.TokenConfig;
import ru.aleks.weather.dto.LocationTransform;
import ru.aleks.weather.exception.FailGetBodyFromResponseException;
import ru.aleks.weather.utils.FormatTransformer;

import java.net.URI;
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

    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?";
    private static final String CITIES_URL = "q=%s&appid=%s";
    private static final String COORDINATES_URL = "lat=%.6f&lon=%.6f&appid=%s";
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherApiService.class);

    public WeatherApiService(TokenConfig tokenConfig, FormatTransformer jsonToDtoTransformer) {
        this.tokenConfig = tokenConfig;
        this.jsonToDtoTransformer = jsonToDtoTransformer;
    }

    public Optional<LocationTransform> getWeatherByCityName(String city) {
        return jsonToDtoTransformer.transform(getBodyFromResponse(
                getUrl(city)
        ));
    }

    public Optional<LocationTransform> getWeatherByCoordinates(double latitude, double longitude) {
        return jsonToDtoTransformer.transform(
                getBodyFromResponse(getUrl(
                        latitude, longitude
                ))
        );
    }

    private String getBodyFromResponse(String url) {
        String body = "";
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .timeout(Duration.of(10, SECONDS))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            body = response.body();
        } catch (Exception ex) {
            LOGGER.warn("WeatherApiService: error get body from response");
            throw new FailGetBodyFromResponseException("error get body from response");
        }
        return body;
    }

    private String getUrl(String cityName) {
        String url = BASE_URL + CITIES_URL;
        return String.format(
                url,
                cityName,
                tokenConfig.getToken());
    }

    private String getUrl(double latitude, double longitude) {
        String coordUrl = BASE_URL + COORDINATES_URL;
        return String.format(
                coordUrl,
                latitude,
                longitude,
                tokenConfig.getToken());
    }
}