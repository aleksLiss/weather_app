package ru.aleks.weather.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.aleks.weather.model.User;

import java.util.Optional;

@Repository
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean save(User user) {
        return jdbcTemplate.update("INSERT INTO Users(login, password) VALUES(?, ?)", new Object[] {user.getLogin(), user.getPassword()}) > 0;
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM Users WHERE id = ?", id) > 0;
    }

    @Override
    public Optional<User> getById(int id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM Users", new BeanPropertyRowMapper<User>(User.class), id));
    }

    @Override
    public Optional<User> getByLogin(String login) {
        return Optional.ofNullable(
                jdbcTemplate.queryForObject("SELECT * FROM Users", new BeanPropertyRowMapper<User>(User.class), login)
        );
    }
}