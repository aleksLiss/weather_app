package ru.aleks.weather.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.aleks.weather.exception.FailedDeleteUserException;
import ru.aleks.weather.exception.UserAlreadyExistsException;
import ru.aleks.weather.exception.UserNotFoundException;
import ru.aleks.weather.mapper.UserMapper;
import ru.aleks.weather.model.User;

import java.util.Optional;

@Repository
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
            throw new UserAlreadyExistsException("user already exists");
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
            throw new FailedDeleteUserException("User with id: " + id + " was not deleted");
        }
        return deleted != 0;
    }

    @Override
    public Optional<User> getById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        User user = null;
        try {
            user = jdbcTemplate.queryForObject(sql, new Object[]{id}, userMapper);
        } catch (Exception ex) {
            LOGGER.warn("UserRepository: User with id: {} not found", id);
            throw new UserNotFoundException("User with id: " + id + "not found");
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
            throw new UserNotFoundException("User with login: " + login + " was not found");
        }
        return Optional.ofNullable(foundUserByLogin);
    }

    @Override
    public boolean deleteByLogin(String login) {
        String sql = "DELETE FROM users WHERE login = ?";
        int deleted;
        try {
            deleted = jdbcTemplate.update(sql, login);
            LOGGER.info("UserRepository: User with login: {} was deleted", login);
        } catch (Exception ex) {
            LOGGER.warn("UserRepository: User with login: {} was not deleted", login);
            throw new FailedDeleteUserException("User with login: " + login + " was not deleted");
        }
        return deleted != 0;
    }
}