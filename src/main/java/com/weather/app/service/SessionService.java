package com.weather.app.service;

import  com.weather.app.model.Session;
import com.weather.app.model.User;
import com.weather.app.repository.SessionRepository;
import com.weather.app.util.cookie.CookieFactory;
import com.weather.app.util.session.NameSessionIdFinder;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final Environment environment;

    public SessionService(SessionRepository sessionRepository, Environment environment) {
        this.sessionRepository = sessionRepository;
        this.environment = environment;
    }

    public void authenticateAndManageSession(User foundUser,
                                             HttpServletResponse response) {
        String nameSessionId = NameSessionIdFinder.getNameSessionId(environment);
        saveAndAddSessionIntoCookie(foundUser, nameSessionId, response);
    }

    public Optional<Session> save(Session session) {
        return Optional.of(sessionRepository.save(session));
    }

    public List<Session> getSessionsByUserId(int id) {
        return sessionRepository.findAllByUserId(id);
    }

    public Optional<Session> getSessionByUUID(String uuid) {
        UUID uuidFromString = UUID.fromString(uuid);
        return sessionRepository.getSessionsByUUID(uuidFromString);
    }

    @Transactional
    public void deleteSessionByUUID(String uuid) {
        UUID uuidFromString = UUID.fromString(uuid);
        sessionRepository.deleteSessionByUUID(uuidFromString);
    }

    private void saveAndAddSessionIntoCookie(User user,
                                             String nameSessionId,
                                             HttpServletResponse response) {
        Optional<Session> customSession = createSession(user);
        if (customSession.isPresent()) {
            sessionRepository.save(customSession.get());
            CookieFactory.createAndAddToResponseCookie(nameSessionId, customSession.orElse(null), response);
        }
    }

    private Optional<Session> createSession(User user) {
        return Optional.of(new Session(
                user,
                LocalDateTime.now().plusHours(1)
        ));
    }
}
