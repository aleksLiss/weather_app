package ru.aleks.weather.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleks.weather.model.Session;
import ru.aleks.weather.repository.SessionRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionService.class);

    @Autowired
    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Optional<Session> save(Session session) {
        return sessionRepository.save(session);
    }

    public Optional<Session> getById(UUID sessionId) {
        return sessionRepository.getById(sessionId);
    }

    public boolean isDeleted(UUID sessionId) {
        return sessionRepository.delete(sessionId);
    }
}
