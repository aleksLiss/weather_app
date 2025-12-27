package com.weather.app.util.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class CookiesFinder {

    private CookiesFinder(){}

    public static Cookie[] getCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        return (cookies != null) ? cookies : new Cookie[0];
    }
}
