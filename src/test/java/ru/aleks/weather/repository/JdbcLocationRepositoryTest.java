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
    private static JdbcUserRepository jdbcUserRepository;

    @BeforeAll
    public static void init() {
        H2DatabaseConfig h2DatabaseConfig = new H2DatabaseConfig();
        DataSource dataSource = h2DatabaseConfig.dataSource();
        JdbcTemplate jdbcTemplate = h2DatabaseConfig.jdbcTemplate(dataSource);
        jdbcLocationRepository = new JdbcLocationRepository(
                jdbcTemplate, new LocationMapper()
        );
        jdbcUserRepository = new JdbcUserRepository(
                jdbcTemplate, new UserMapper()
        );
    }

    @AfterEach
    public void clear() {
        jdbcUserRepository.deleteByLogin("vova");
        jdbcLocationRepository.deleteByUserId(1);
    }

    @Test
    public void whenSavedLocationAndGetItByNameThenReturnSavedLocation() {
        Optional<User> savedUser = jdbcUserRepository.getByLogin("vova");
        if (savedUser.isEmpty()) {
            User user = new User("vova", "123");
            savedUser = jdbcUserRepository.save(user);
        }
        assertThat(savedUser).isPresent();
        jdbcLocationRepository.deleteByUserId(savedUser.get().getId());
        Location location = new Location(
                "minsk",
                savedUser.get().getId(),
                42.234,
                42.234
        );

        Optional<Location> savedLocation = jdbcLocationRepository.save(location);
        assertThat(savedLocation).isPresent();
        Optional<Location> foundLocation = jdbcLocationRepository.getByName("minsk");
        assertThat(foundLocation).isPresent();
        assertThat(foundLocation.get().getName()).isEqualTo(location.getName());
    }

    @Test
    public void whenDontSavedLocationAndGetItByNameThenReturnEmptyOptional() {
        assertThatThrownBy(() -> jdbcLocationRepository.getByName("123"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("error found location by name");
    }

    @Test
    public void whenSavedLocationAndGetItByIdThenReturnSavedLocation() {
        User user = new User("vova", "123");
        Optional<User> savedUser = jdbcUserRepository.save(user);
        assertThat(savedUser).isPresent();
        Location location = new Location(
                "minsk",
                savedUser.get().getId(),
                42.234,
                42.234
        );
        Optional<Location> savedLocation = jdbcLocationRepository.save(location);
        assertThat(savedLocation).isPresent();
        assertThat(jdbcLocationRepository.getById(savedLocation.get().getId())).isPresent();
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
        Optional<User> savedUser = jdbcUserRepository.getByLogin("vova");
        if (savedUser.isEmpty()) {
            User user = new User("vova", "123");
            savedUser = jdbcUserRepository.save(user);
        }
        assertThat(savedUser).isPresent();
        jdbcLocationRepository.deleteByUserId(savedUser.get().getId());
        Location location = new Location(
                "minsk",
                savedUser.get().getId(),
                42.234,
                42.234
        );
        Location location2 = new Location(
                "gomel",
                savedUser.get().getId(),
                42.234,
                42.234
        );
        Optional<Location> savedLocation = jdbcLocationRepository.save(location);
        Optional<Location> savedLocation2 = jdbcLocationRepository.save(location2);
        assertThat(savedLocation).isPresent();
        assertThat(savedLocation2).isPresent();
        assertThat(jdbcLocationRepository.getAllByUserId(savedUser.get().getId()))
                .isNotEmpty()
                .hasSize(2)
                .containsExactly(savedLocation.get(), savedLocation2.get());
    }

    @Test
    public void whenDontSavedSomeLocationsAndGetAllThenReturnEmptyList() {
        assertThat(jdbcLocationRepository.getAllByUserId(1))
                .isEmpty();
    }
}