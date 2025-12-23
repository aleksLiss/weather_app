package com.weather.app.util.session;

import com.weather.app.model.Session;
import com.weather.app.service.SessionService;
import com.weather.app.util.cookie.CookiesFinder;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.env.Environment;

import java.util.Optional;

public class SessionFinder {

    private SessionFinder() {
    }

    public static Optional<Session> findSession(HttpServletRequest request,
                                                Environment environment,
                                                SessionService sessionService) {
        Cookie[] cookies = CookiesFinder.getCookies(request);
        String nameSessionId = NameSessionIdFinder.getNameSessionId(environment);
        String sessionId = getSessionIdFromCookie(cookies, nameSessionId);
        if (null == sessionId) {
            return Optional.empty();
        }
        return sessionService.getSessionByUUID(sessionId);
    }

    private static String getSessionIdFromCookie(Cookie[] cookies, String nameSessionId) {
        String sessionId = null;
        for (Cookie cookie : cookies) {
            if (nameSessionId.equals(cookie.getName())) {
                sessionId = cookie.getValue();
                break;
            }
        }
        return sessionId;
    }
}
