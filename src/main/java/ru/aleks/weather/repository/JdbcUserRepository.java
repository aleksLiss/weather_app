package ru.aleks.weather.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.aleks.weather.mapper.UserMapper;
import ru.aleks.weather.model.User;

import java.util.Optional;

@Component
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcUserRepository.class);

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, UserMapper userMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userMapper = userMapper;
    }

    @Override
    public Optional<User> save(User user) {
        String sql = "INSERT INTO users(login, password) VALUES(?, ?)";
        Optional<User> savedUser = Optional.empty();
        try {
            jdbcTemplate.update(
                    sql,
                    user.getLogin(),
                    user.getPassword()
            );
            savedUser = Optional.ofNullable(
                    jdbcTemplate
                            .queryForObject(
                                    "SELECT * FROM users WHERE LOGIN = ?", new Object[]{user.getLogin()}, userMapper
                            ));
            LOGGER.info("UserRepository: User was saved");
        } catch (DuplicateKeyException ex) {
            LOGGER.warn("UserRepository: This user already exists");
        }
        return savedUser;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        int deleted = 0;
        try {
            deleted = jdbcTemplate.update(sql, id);
            LOGGER.info("UserRepository: User with id: {} was deleted", id);
        } catch (Exception ex) {
            LOGGER.warn("UserRepository: User with id: {} was not deleted", id);
        }
        return deleted != 0;
    }

    @Override
    public Optional<User> getById(int id) {
        String sql = "SELECT id, login, password FROM users WHERE id = ?";
        User user = null;
        try {
            user = jdbcTemplate.queryForObject(sql, new Object[]{id}, userMapper);
        } catch (Exception ex) {
            LOGGER.warn("UserRepository: Course with id: {} not found", id);
        }
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> getByLogin(String login) {
        String sql = "SELECT id, login, password FROM users WHERE login = ?";
        User foundUserByLogin = null;
        try {
            foundUserByLogin = jdbcTemplate.queryForObject(sql, new Object[]{login}, userMapper);
        } catch (Exception ex) {
            LOGGER.warn("UserRepository: User with login: {} was not found", login);
        }
        return Optional.ofNullable(foundUserByLogin);
    }
}