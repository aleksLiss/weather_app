package com.weather.app.util.session;

import com.weather.app.exception.session.NameSessionIdNotFound;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NameSessionIdFinderTest {

    @Mock
    private Environment environment;

    @Test
    void whenNameSessionIdIsPresentThenReturnNameSessionId() {
        String nameSessionId = "test_name";
        String nameProperty = "spring.application.session.id.name";
        when(environment.getProperty(nameProperty)).thenReturn(nameSessionId);
        String expectedNameSessionId = NameSessionIdFinder.getNameSessionId(environment);
        assertThat(nameSessionId).isEqualTo(expectedNameSessionId);
    }

    @Test
    void whenNameSessionIdIsNullThenThrowException() {
        String nameProperty = "spring.application.session.id.name";
        when(environment.getProperty(nameProperty)).thenReturn(null);
        assertThatThrownBy(() -> NameSessionIdFinder.getNameSessionId(environment))
                .isInstanceOf(NameSessionIdNotFound.class)
                .hasMessage("Name session id was not found");
        verify(environment, times(1)).getProperty("spring.application.session.id.name");
    }
}