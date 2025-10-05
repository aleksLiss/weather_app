package ru.aleks.weather.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.aleks.weather.config.TokenConfig;
import ru.aleks.weather.dto.LocationAnswerDto;

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

    private TokenConfig tokenConfig;
    private static final String URL = "https://api.openweathermap.org/data/2.5/weather?";
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherApiService.class);


    public WeatherApiService(TokenConfig tokenConfig) {
        this.tokenConfig = tokenConfig;
    }

    public Optional<LocationAnswerDto> getWeatherByName(String city) throws URISyntaxException {
        LocationAnswerDto locationAnswerDto = new LocationAnswerDto();
        String url = String.format(
                "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s",
                city,
                tokenConfig.getToken());
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .timeout(Duration.of(10, SECONDS))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            locationAnswerDto.setCity();
//            locationAnswerDto.setCountry();
//            locationAnswerDto.setTemperature();
//            locationAnswerDto.setHumidity();
//            locationAnswerDto.setDescription();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(locationAnswerDto);
    }
    /*
    public Optional<LocationAnswerDto> getWeatherByLocation(double latitude, double longitude) throws URISyntaxException {
        LocationAnswerDto locationAnswerDto = new LocationAnswerDto();
        String url = String.format(
                "https://api.openweathermap.org/data/2.5/weather?lat=%.6f&lon=%.6f&appid=%s",
                latitude,
                longitude,
                tokenProvider.getToken());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .timeout(Duration.of(10, SECONDS))
                .GET()
                .build();
        return Optional.of(locationAnswerDto);
    }

//    private double fromFarengeitsToCelcius()

     */
}