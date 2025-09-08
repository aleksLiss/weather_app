package ru.aleks.weather.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.aleks.weather.mapper.SessionMapper;
import ru.aleks.weather.model.Session;

import java.util.Optional;
import java.util.UUID;

@Repository
public class JdbcSessionRepository implements SessionRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SessionMapper sessionMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcSessionRepository.class);

    @Autowired
    public JdbcSessionRepository(JdbcTemplate jdbcTemplate, SessionMapper sessionMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.sessionMapper = sessionMapper;
    }

    @Override
    public Optional<Session> save(Session session) {
        String sql = "INSERT INTO sessions(id, user_id, expires_at) VALUES(?, ?, ?)";
        Optional<Session> savedSession = Optional.empty();
        try {
            jdbcTemplate.update(sql,
                    session.getId(),
                    session.getUserId(),
                    session.getExpiresAt()
            );
            savedSession = Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            "SELECT * FROM sessions WHERE id = ?",
                            new Object[]{session.getId()}, sessionMapper
                    )
            );
            LOGGER.info("SessionRepository: session was saved");
        } catch (Exception exception) {
            LOGGER.warn("SessionRepository: session was not saved");
        }
        return savedSession;
    }

    @Override
    public Optional<Session> getById(UUID sessionId) {
        String sql = "SELECT * FROM sessions WHERE id = ?";
        Optional<Session> foundSession = Optional.empty();
        try {
            foundSession = Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            sql,
                            new Object[]{sessionId},
                            sessionMapper
                    ));
            LOGGER.info("SessionRepository: session was found");
        } catch (Exception ex) {
            LOGGER.warn("SessionRepository: session was not found");
        }
        return foundSession;
    }

    @Override
    public boolean delete(UUID sessionId) {
        String sql = "DELETE FROM sessions WHERE id = ?";
        int deleted = 0;
        try {
            deleted = jdbcTemplate.update(sql, sessionId);
            LOGGER.info("SessionRepository: session with id {} was deleted", sessionId);
        } catch (Exception ex) {
            LOGGER.warn("SessionRepository: session with id {} was not deleted", sessionId);
        }
        return deleted != 0;
    }
}
