package com.weather.app.repository;

import com.weather.app.model.User;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {com.weather.app.Main.class})
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    private User user;
    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        user = new User();
        user.setLogin("ivan_ivanov");
        user.setPassword("123");
        user = userRepository.save(user);
    }

    @AfterEach
    void clearRepository() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should get saved user by id")
    void whenSaveUserAndFindItByIdThenReturnSavedUser() {
        Optional<User> foundUser = userRepository.findById(user.getId());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getLogin()).isEqualTo("ivan_ivanov");
    }

    @Test
    @DisplayName("Should get empty optional when user was not saved")
    void whenDontSaveUserAndFindItByIdThenReturnEmptyOptional() {
        Optional<User> result = userRepository.getUserByLogin("non_existent_user");
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should get saved user by login")
    void whenSaveUserAndFindItByLoginThenReturnSavedUser() {
        Optional<User> result = userRepository.getUserByLogin(user.getLogin());
        assertThat(result).isPresent();
        assertThat(result.get().getLogin()).isEqualTo(user.getLogin());
    }

    @Test
    @DisplayName("Should throw exception when save user with equal login")
    void whenSaveUserWithEqualLoginThenThrowException() {
        User user2 = new User();
        user2.setLogin("ivan_ivanov");
        user2.setPassword("123");
        assertThatThrownBy(() -> {
            entityManager.persistAndFlush(user2);
        }).isInstanceOf(PersistenceException.class);
        entityManager.clear();
    }

    @Test
    @DisplayName("Should save user with equals passwords")
    void whenSaveUserWithEqualPasswordThenOk() {
        User user2 = new User();
        user2.setLogin("ivan_ivanov2");
        user2.setPassword("123");
        userRepository.save(user2);
        assertThat(userRepository.getUserByLogin(user.getLogin())).isPresent();
        assertThat(userRepository.getUserByLogin(user2.getLogin())).isPresent();
    }

    @Test
    @DisplayName("Delete user after save it")
    void whenSaveUserAndDeleteItThenOk() {
        userRepository.delete(user);
        Optional<User> result = userRepository.findById(user.getId());
        assertThat(result).isEmpty();
    }
}
