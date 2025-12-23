package com.weather.app.interceptor;

import com.weather.app.service.SessionService;
import com.weather.app.util.session.TimeSessionChecker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessionTimeInterceptor implements HandlerInterceptor {

    private final SessionService sessionService;
    private final Environment environment;

    public SessionTimeInterceptor(SessionService sessionService, Environment environment) {
        this.sessionService = sessionService;
        this.environment = environment;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        TimeSessionChecker.checkSessionTime(request, response, sessionService, environment);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
