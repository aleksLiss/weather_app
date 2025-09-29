package ru.aleks.weather.repository;

import ru.aleks.weather.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> save(User user);

    boolean delete(int id);

    Optional<User> getById(int id);

    Optional<User> getByLogin(String login);

    boolean deleteByLogin(String login);
}
