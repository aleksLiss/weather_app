package com.weather.app.util;

import org.junit.jupiter.api.Test;

import java.net.URLEncoder;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.*;

class UrlFactoryTest {

    @Test
    void whenGetUrlForOneLocationThenReturnFormattedUrl() {
        String token = "test_token";
        String baseUrl = "https://api.openweathermap.org/data/2.5/weather?lat=%.6f&lon=%.6f&appid=%s";
        double latitude = 22.222;
        double longitude = 11.111;
        String expectedUrl = "https://api.openweathermap.org/data/2.5/weather?lat=22,222000&lon=11,111000&appid=test_token";
        String result = String.format(baseUrl, latitude, longitude, token);
        assertThat(result).isEqualTo(expectedUrl);
    }

    @Test
    void whenGetUrlForOneLocationAndParametersAreEmptyThenReturnUnformattedUrl() {
        String token = "";
        String baseUrl = "https://api.openweathermap.org/data/2.5/weather?lat=%.6f&lon=%.6f&appid=%s";
        double latitude = 0.0;
        double longitude = 0.0;
        String expectedUrl = "https://api.openweathermap.org/data/2.5/weather?lat=0,000000&lon=0,000000&appid=";
        String result = String.format(baseUrl, latitude, longitude, token);
        assertThat(result).isEqualTo(expectedUrl);
    }

    @Test
    void whenGetUrlForLocationListThenReturnFormattedUtl() {
        String token = "test_token";
        String city = "gomel";
        int limit = 5;
        String encodedCity = URLEncoder.encode(city, UTF_8);
        String baseUrl = "https://api.openweathermap.org/geo/1.0/direct?q=%s&limit=%s&appid=%s";
        String expectedUrl = "https://api.openweathermap.org/geo/1.0/direct?q=gomel&limit=5&appid=test_token";
        String result = String.format(baseUrl, encodedCity, limit, token);
        assertThat(result).isEqualTo(expectedUrl);
    }

    @Test
    void whenGetUrlForLocationListAndEmptyParametersThenReturnFormattedUtl() {
        String token = "";
        String city = "";
        int limit = 0;
        String baseUrl = "https://api.openweathermap.org/geo/1.0/direct?q=%s&limit=%s&appid=%s";
        String expectedUrl = "https://api.openweathermap.org/geo/1.0/direct?q=&limit=0&appid=";
        String encodedCity = URLEncoder.encode(city, UTF_8);
        String result = String.format(baseUrl, encodedCity, limit, token);
        assertThat(result).isEqualTo(expectedUrl);
    }
}