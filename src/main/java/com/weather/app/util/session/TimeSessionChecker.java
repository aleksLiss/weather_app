package com.weather.app.util.session;

import com.weather.app.model.Session;
import com.weather.app.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;

import java.util.Optional;

public class TimeSessionChecker {

    private TimeSessionChecker() {}

    public static void checkSessionTime(HttpServletRequest request,
                                        HttpServletResponse response,
                                        SessionService sessionService,
                                        Environment environment) {
        Optional<Session> sessionOptional = SessionFinder.findSession(
                request,
                environment,
                sessionService
        );
        sessionOptional.ifPresent(session -> SessionTimeValidator.checkValidSessionTime(
                request,
                response,
                environment,
                session
        ));
    }
}
