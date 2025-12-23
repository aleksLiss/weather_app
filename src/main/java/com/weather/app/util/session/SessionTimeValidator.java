package com.weather.app.util.session;

import com.weather.app.model.Session;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;

import java.time.Duration;
import java.time.LocalDateTime;

public class SessionTimeValidator {

    private SessionTimeValidator() {
    }

    public static String checkValidSessionTime(HttpServletRequest request,
                                               HttpServletResponse response,
                                               Environment environment,
                                               Session session) {
        if (!isSessionTimeStillValid(session)) {
            SessionDestroyer.invalidateSession(
                    request,
                    response,
                    environment
            );
        }
        return "redirect:/sign/in";
    }

    static boolean isSessionTimeStillValid(Session session) {
        LocalDateTime oldTimeSession = session.getExpiresAt();
        return Duration.between(LocalDateTime.now(), oldTimeSession).getSeconds() < 3600;
    }
}
