package com.weather.app.util.cookie;

import com.weather.app.exception.CookiesNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class CookiesFinder {

    private CookiesFinder(){}

    public static Cookie[] getCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (null == cookies) {
            throw new CookiesNotFoundException("Cookies was not found");
        }
        return cookies;
    }
}
