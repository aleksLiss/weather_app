package com.weather.app.util.cookie;

import com.weather.app.model.Session;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieFactory {

    private CookieFactory() {
    }

    public static void createAndAddToResponseCookie(String nameSessionId, Session session, HttpServletResponse response) {
        Cookie cookie = new Cookie(nameSessionId, String.valueOf(session.getId()));
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60);
        response.addCookie(cookie);
    }
}
