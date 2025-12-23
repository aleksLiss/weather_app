package com.weather.app.util;

import java.net.URLEncoder;

import static java.nio.charset.StandardCharsets.*;

public class UrlFactory {

    private UrlFactory() {
    }

    public static String getUrlForOneLocation(String baseUrl, double latitude, double longitude, String token) {
        return String.format(baseUrl, latitude, longitude, token);
    }

    public static String getUrlForLocationList(String baseUrl, String city, int limit, String token) {
        String encodedCity = URLEncoder.encode(city, UTF_8);
        return String.format(baseUrl, encodedCity, limit, token);
    }
}
