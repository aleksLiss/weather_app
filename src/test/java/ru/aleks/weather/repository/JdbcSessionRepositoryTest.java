package ru.aleks.weather.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import ru.aleks.weather.config.H2DatabaseConfig;
import ru.aleks.weather.exception.SessionNotFoundException;
import ru.aleks.weather.mapper.SessionMapper;
import ru.aleks.weather.mapper.UserMapper;
import ru.aleks.weather.model.Session;
import ru.aleks.weather.model.User;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class JdbcSessionRepositoryTest {

    private static JdbcSessionRepository jdbcSessionRepository;
    private static JdbcUserRepository jdbcUserRepository;
    private static final UUID uuid = UUID.randomUUID();
    private User user;

    @BeforeAll
    public static void setUp() {
        H2DatabaseConfig h2DatabaseConfig = new H2DatabaseConfig();
        DataSource dataSource = h2DatabaseConfig.dataSource();
        JdbcTemplate jdbcTemplate = h2DatabaseConfig.jdbcTemplate(dataSource);
        jdbcSessionRepository = new JdbcSessionRepository(
                jdbcTemplate, new SessionMapper()
        );
        jdbcUserRepository = new JdbcUserRepository(
                jdbcTemplate, new UserMapper()
        );
    }

    @BeforeEach
    public void save() {
        if (jdbcUserRepository.getByLogin("vova").isEmpty()) {
            user = jdbcUserRepository.save(new User("vova", "123")).get();
        }
    }

    @AfterEach
    public void clear() {
        if (user.getLogin() != null) {
            jdbcUserRepository.deleteByLogin("vova");
        }
    }

    @Test
    public void whenSavedSessionThenReturnSavedSession() {
        Session session = new Session(
                uuid,
                user.getId(),
                LocalDateTime.now()
        );
        Optional<Session> savedSession = jdbcSessionRepository.save(session);
        assertThat(savedSession).isPresent();
    }

    @Test
    public void whenSavedSessionAndGetItByIdThenReturnSavedSession() {
        Session session = new Session(
                uuid,
                user.getId(),
                LocalDateTime.now()
        );
        Optional<Session> savedSession = jdbcSessionRepository.save(session);
        assertThat(savedSession).isPresent();
        Optional<Session> foundSession = jdbcSessionRepository.getById(uuid);
        assertThat(foundSession).isPresent();
        assertThat(foundSession.get().getUserId()).isEqualTo(user.getId());
    }


    @Test
    public void whenDontSavedSessionAndGetItByIdThenThrowException() {
        assertThatThrownBy(() -> jdbcSessionRepository.getById(UUID.randomUUID()))
                .isInstanceOf(SessionNotFoundException.class)
                .hasMessage("session was not found");
    }
}