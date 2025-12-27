package com.weather.app.util.session;

import com.weather.app.exception.session.NameSessionIdNotFound;
import org.springframework.core.env.Environment;

public class NameSessionIdFinder {

    private NameSessionIdFinder(){}

    public static String getNameSessionId(Environment environment) {
        String nameSessionId = environment.getProperty("application.session.id.name");
        if (null == nameSessionId) {
            throw new NameSessionIdNotFound("Name session id was not found");
        }
        return nameSessionId;
    }
}
