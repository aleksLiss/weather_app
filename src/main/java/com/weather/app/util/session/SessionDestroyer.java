package com.weather.app.util.session;

import com.weather.app.util.cookie.CookiesFinder;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;

public class SessionDestroyer {

    private SessionDestroyer() {
    }

    public static void invalidateSession(HttpServletRequest request, HttpServletResponse response, Environment environment) {
        Cookie[] cookies = CookiesFinder.getCookies(request);
        String nameSessionId = NameSessionIdFinder.getNameSessionId(environment);
        for (Cookie cookie : cookies) {
            if (nameSessionId.equals(cookie.getName())) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
                break;
            }
        }
    }
}
