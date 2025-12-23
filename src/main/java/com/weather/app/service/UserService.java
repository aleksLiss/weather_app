package com.weather.app.service;

import com.weather.app.dto.SignInUserDto;
import com.weather.app.exception.password.IncorrectPasswordException;
import com.weather.app.exception.username.UserAlreadyExistsException;
import com.weather.app.exception.username.UserNotFoundException;
import com.weather.app.model.User;
import com.weather.app.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> save(User user) {
        String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(10));
        user.setPassword(hashed);
        try {
            return Optional.of(userRepository.save(user));
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException("User already exists");
        }
    }

    public Optional<User> getUserByLogin(SignInUserDto signInUserDto) {
        Optional<User> foundUser = userRepository.getUserByLogin(signInUserDto.login());
        if (foundUser.isEmpty()) {
            throw new UserNotFoundException("User by login was not found");
        }
        foundUser.ifPresent(user -> checkCorrectPassword(signInUserDto.password(), user.getPassword()));
        return foundUser;
    }

    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    public void checkCorrectPassword(String plainTextPassword, String storedHash) {
        if (!BCrypt.checkpw(plainTextPassword, storedHash)) {
            LOGGER.warn("input password not correct");
            throw new IncorrectPasswordException("Input password not correct");
        }
    }
}
