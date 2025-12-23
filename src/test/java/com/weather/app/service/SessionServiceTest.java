package com.weather.app.service;

import com.weather.app.model.Session;
import com.weather.app.model.User;
import com.weather.app.repository.SessionRepository;
import com.weather.app.util.session.NameSessionIdFinder;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private HttpServletResponse response;
    @Mock
    private User user;
    @Mock
    private Session session;
    @InjectMocks
    private SessionService sessionService;

    @Test
    void whenAuthenticateAndManageSessionByFoundUserThenOk() {
        try (MockedStatic<NameSessionIdFinder> mockedStatic = mockStatic(NameSessionIdFinder.class)) {
            mockedStatic.when(() -> NameSessionIdFinder.getNameSessionId(any())).thenReturn("sessionId");
            sessionService.authenticateAndManageSession(any(User.class), response);
            ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
            verify(response).addCookie(cookieCaptor.capture());
            Cookie cookie = cookieCaptor.getValue();
            assertEquals("sessionId", cookie.getName());
        }
    }

    @Test
    void whenSaveSessionAndGetItByIdThenReturnSavedSession() {
        when(sessionRepository.save(any(Session.class))).thenReturn(session);
        Optional<Session> savedSession = sessionService.save(session);
        assertThat(savedSession).isPresent();
        assertThat(savedSession.get()).isEqualTo(session);
    }

    @Test
    void whenDontSaveSessionAndGetItByIdThenReturnEmptyOptional() {
        assertThat(sessionService.getSessionByUUID(UUID.randomUUID().toString())).isEmpty();
    }

    @Test
    void whenSaveSessionAndGetItByUserIdThenReturnListOfSavedSessions() {
        when(sessionRepository.save(any(Session.class))).thenReturn(session);
        sessionService.save(session);
        when(sessionRepository.findAllByUserId(anyInt())).thenReturn(List.of(session));
        List<Session> sessions = sessionService.getSessionsByUserId(user.getId());
        assertThat(sessions)
                .isNotEmpty()
                .hasSize(1)
                .containsExactlyInAnyOrder(session);
    }

    @Test
    void whenDontSaveSessionAndGetItByUserIdThenReturnEmptyList() {
        List<Session> sessions = sessionService.getSessionsByUserId(user.getId());
        assertThat(sessions)
                .isEmpty();
    }

    @Test
    void whenSaveSessionAndDeleteItThenOk() {
        UUID id = UUID.randomUUID();
        doNothing()
                .when(sessionRepository)
                .deleteSessionByUUID(any(UUID.class));
        sessionService.deleteSessionByUUID(id.toString());
        verify(sessionRepository, times(1))
                .deleteSessionByUUID(eq(id));
    }
}