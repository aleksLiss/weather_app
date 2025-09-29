package ru.aleks.weather.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import ru.aleks.weather.config.H2DatabaseConfig;
import ru.aleks.weather.mapper.UserMapper;
import ru.aleks.weather.model.User;

import javax.sql.DataSource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class JdbcUserRepositoryTest {

    @Autowired
    private static JdbcUserRepository jdbcUserRepository;

    @BeforeAll
    public static void init() {
        H2DatabaseConfig h2DatabaseConfig = new H2DatabaseConfig();
        DataSource dataSource = h2DatabaseConfig.dataSource();
        JdbcTemplate jdbcTemplate = h2DatabaseConfig.jdbcTemplate(dataSource);
        jdbcUserRepository = new JdbcUserRepository(
            jdbcTemplate, new UserMapper()
        );
    }

    @AfterEach
    public void clear() {
        jdbcUserRepository.deleteByLogin("123");
    }

    @Test
    public void whenSaveUserAndGetByLoginThenReturnSavedUser() {
        Optional<User> savedUser = jdbcUserRepository.save(
                new User("123", "123")
        );
        assertThat(savedUser.get().getLogin()).isEqualTo("123");
    }

    @Test
    public void whenDontSavedUserAndGetByLoginThenReturnEmptyOptional() {
        assertThat(jdbcUserRepository.getByLogin("")).isEqualTo(Optional.empty());
    }

    @Test
    public void whenSavedUserWithEqualsLoginThenThrowException() {
        User user = new User("123", "123");
        Optional<User> savedUser = jdbcUserRepository.save(
                user
        );
        assertThatThrownBy(() -> jdbcUserRepository.save(user))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("user already exists");
    }

    @Test
    public void whenSavedUserAndGetByIdThenReturnSavedUser() {
        User user = new User("123", "123");
        Optional<User> savedUser = jdbcUserRepository.save(
                user
        );
        assertThat(savedUser.get().getId()).isEqualTo(savedUser.get().getId());
    }

    @Test
    public void whenDontSavedUserAndGetByIdThenReturnEmptyOptional() {
        assertThat(jdbcUserRepository.getById(1)).isEqualTo(Optional.empty());
    }
}