package ru.aleks.weather.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.aleks.weather.model.Session;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Component
public class SessionMapper implements RowMapper<Session> {
    @Override
    public Session mapRow(ResultSet rs, int rowNum) throws SQLException {
        Session session = new Session();
        session.setId(UUID.fromString(rs.getString("id")));
        session.setUserId(rs.getInt("user_id"));
        session.setExpiresAt(rs.getObject("expires_at", LocalDateTime.class));
        return session;
    }
}
