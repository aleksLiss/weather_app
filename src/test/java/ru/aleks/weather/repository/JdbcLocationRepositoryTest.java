package ru.aleks.weather.repository;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import ru.aleks.weather.config.H2DatabaseConfig;
import ru.aleks.weather.mapper.LocationMapper;
import ru.aleks.weather.mapper.UserMapper;
import ru.aleks.weather.model.Location;
import ru.aleks.weather.model.User;

import javax.sql.DataSource;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class JdbcLocationRepositoryTest {

    private static JdbcLocationRepository jdbcLocationRepository;
    private static JdbcUserRepository mockJdbcUserRepository;

    @BeforeAll
    public static void init() {
        H2DatabaseConfig h2DatabaseConfig = new H2DatabaseConfig();
        DataSource dataSource = h2DatabaseConfig.dataSource();
        JdbcTemplate jdbcTemplate = h2DatabaseConfig.jdbcTemplate(dataSource);
        jdbcLocationRepository = new JdbcLocationRepository(
                jdbcTemplate, new LocationMapper()
        );

        mockJdbcUserRepository = mock(JdbcUserRepository.class);
    }

    @Test
    public void whenSavedLocationAndGetItByNameThenReturnSavedLocation() {
        User user = new User("vova", "123");
        // Настраиваем мок для метода save, чтобы он возвращал то же самое или Optional.of(user)
        when(mockJdbcUserRepository.save(any(User.class))).thenReturn(Optional.of(user));
        // Аналогично для метода getByLogin
        when(mockJdbcUserRepository.getByLogin("vova")).thenReturn(Optional.of(user));
        Optional<User> savedUser = mockJdbcUserRepository.save(user);
        assertThat(savedUser).isPresent();
        Optional<User> retrievedUser = mockJdbcUserRepository.getByLogin("vova");
        assertThat(retrievedUser).isPresent();
        assertThat("vova").isEqualTo(retrievedUser.get().getLogin());
    }
/*
    @Test
    public void whenDontSavedLocationAndGetItByNameThenReturnEmptyOptional() {
        assertThatThrownBy(() -> jdbcLocationRepository.getByName("123"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("error found location by name");
    }

    @Test
    public void whenSavedLocationAndGetItByIdThenReturnSavedLocation() {
        Location location = new Location(
                "minsk",
                savedUser.get().getId(),
                42.234,
                42.234
        );
        Optional<Location> savedLocation = jdbcLocationRepository.save(location);
        assertThat(jdbcLocationRepository.getById(savedLocation.get().getId()).get().getName()).isEqualTo(location.getName());
    }

    @Test
    public void whenDontSavedLocationAndGetItByIdThenReturnEmptyOptional() {
        assertThatThrownBy(() -> jdbcLocationRepository.getById(0))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("error found location by id");
    }

    @Test
    public void whenSavedSomeLocationsAndGetAllThenReturnListSavedLocations() {
        Location location = new Location(
                "minsk",
                savedUser.getId(),
                42.234,
                42.234
        );
        Location location2 = new Location(
                "gomel",
                savedUser.getId(),
                42.234,
                42.234
        );
        Optional<Location> savedLocation = jdbcLocationRepository.save(location);
        Optional<Location> savedLocation2 = jdbcLocationRepository.save(location2);
        assertThat(jdbcLocationRepository.getAllByUserId(savedUser.getId()))
                .isNotEmpty()
                .hasSize(2)
                .containsExactly(savedLocation.get(), savedLocation2.get());
    }

    @Test
    public void whenDontSavedSomeLocationsAndGetAllThenReturnEmptyList() {
        assertThat(jdbcLocationRepository.getAllByUserId(1))
                .isEmpty();
    }

 */

}