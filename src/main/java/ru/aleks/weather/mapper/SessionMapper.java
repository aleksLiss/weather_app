package ru.aleks.weather.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.aleks.weather.model.Session;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class SessionMapper implements RowMapper<Session> {
    @Override
    public Session mapRow(ResultSet rs, int rowNum) throws SQLException {
        Session session = new Session();
        session.setId(UUID.fromString(String.valueOf(rs.getInt("id"))));
        session.setUserId(rs.getInt("user_id"));
        session.setExpiresAt(
                LocalDateTime.ofInstant(
                        rs.getDate("expires_at").toInstant(),
                        ZoneId.systemDefault())
        );
        return session;
    }
}
