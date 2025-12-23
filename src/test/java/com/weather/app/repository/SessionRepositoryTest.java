package com.weather.app.repository;

import com.weather.app.model.Session;
import com.weather.app.model.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {com.weather.app.Main.class})
class SessionRepositoryTest {

    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private UserRepository userRepository;
    private User user;

    @BeforeEach
    void setUp() {
        sessionRepository.deleteAll();
        userRepository.deleteAll();
        user = new User();
        user.setLogin("vova");
        user.setPassword("123");
        user = userRepository.save(user);
    }

    @AfterEach
    void deleteUser() {
        sessionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should get saved session by id")
    void whenSaveSessionAndGetItByIdThenReturnSavedSession() {
        Session session = new Session();
        session.setUser(user);
        session.setExpiresAt(LocalDateTime.now().plusHours(1));
        Session savedSession = sessionRepository.save(session);
        Optional<Session> foundSession = sessionRepository.getSessionsByUUID(savedSession.getId());
        assertThat(foundSession).isPresent();
        assertThat(foundSession.get().getUser().getLogin()).isEqualTo("vova");
    }

    @Test
    @DisplayName("Should get empty optional")
    void whenDontSaveSessionAndGetItByIdThenReturnEmptyOptional() {
        assertThat(sessionRepository.getSessionsByUUID(UUID.randomUUID())).isEmpty();
    }

    @Test
    @DisplayName("Should delete session by id")
    void whenSaveSessionAndDeleteItByIdThenOk() {
        Session session = new Session();
        session.setUser(user);
        session.setExpiresAt(LocalDateTime.now().plusHours(1));
        Session savedSession = sessionRepository.save(session);
        sessionRepository.deleteSessionByUUID(savedSession.getId());
        assertThat(sessionRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("Should get all sessions by user id")
    void whenSavedSomeSessionAndGetItByUserIdThenReturnListSessions() {
        Session session1 = new Session();
        Session session2 = new Session();
        session1.setUser(user);
        session2.setUser(user);
        session1.setExpiresAt(LocalDateTime.now().plusHours(1));
        session2.setExpiresAt(LocalDateTime.now().plusHours(1));
        Session savedSession1 = sessionRepository.save(session1);
        Session savedSession2 = sessionRepository.save(session2);
        assertThat(sessionRepository.findAllByUserId(user.getId()))
                .isNotEmpty()
                .hasSize(2)
                .containsExactlyInAnyOrder(savedSession1, savedSession2);
    }
}