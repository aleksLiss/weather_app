package ru.aleks.weather.repository;

import ru.aleks.weather.model.Session;

import java.util.Optional;
import java.util.UUID;

public interface SessionRepository {

    Optional<Session> save(Session session);

    Optional<Session> getById(UUID sessionId);

    boolean delete(UUID sessionId);
}
