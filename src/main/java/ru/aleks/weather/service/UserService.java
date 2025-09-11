package ru.aleks.weather.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.aleks.weather.dto.SaveUserDto;
import ru.aleks.weather.model.User;
import ru.aleks.weather.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> save(SaveUserDto saveUserDto) {
        Optional<User> savedUser = Optional.empty();
        try {
            savedUser = userRepository.save(
                    new User(
                            saveUserDto.getUsername(),
                            saveUserDto.getPassword()
                    )
            );
        } catch (Exception ex) {
            LOGGER.warn("UserService: User was not saved");
        }
        return savedUser;
    }

    public Optional<User> getUserByLogin(String login) {
        return userRepository.getByLogin(login);
    }
}
