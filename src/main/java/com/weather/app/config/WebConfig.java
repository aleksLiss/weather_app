package com.weather.app.config;

import com.weather.app.interceptor.SessionTimeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final SessionTimeInterceptor sessionTimeInterceptor;

    public WebConfig(SessionTimeInterceptor sessionTimeInterceptor) {
        this.sessionTimeInterceptor = sessionTimeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionTimeInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/sign/**", "/static/**");
    }
}
