package com.weather.app.repository;

import com.weather.app.model.Location;
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

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {com.weather.app.Main.class})
class LocationRepositoryTest {

    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager entityManager;
    private User user;
    private Location location;

    @BeforeEach
    void setUp() {
        locationRepository.deleteAll();
        userRepository.deleteAll();
        user = new User();
        user.setLogin("login");
        user.setPassword("123");
        user = userRepository.save(user);
        location = new Location();
        location.setUser(user);
        location.setName("gomel");
        location.setLatitude(52.424160);
        location.setLongitude(31.014281);
        location = locationRepository.save(location);
    }

    @AfterEach
    void clearRepositories() {
        locationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should get locations by user")
    void whenSaveLocationAndThenReturnByName() {
        assertThat(locationRepository.getLocationsByUser(user))
                .isNotEmpty()
                .hasSize(1)
                .containsExactlyInAnyOrder(location);
    }

    @Test
    @DisplayName("Should get empty list")
    void whenDontSaveLocationAndGetItByUserThenReturnEmptyList() {
        User user2 = new User();
        user2.setLogin("vova");
        user2.setPassword("444");
        userRepository.save(user2);
        assertThat(locationRepository.getLocationsByUser(user2))
                .isEmpty();
    }

    @Test
    @DisplayName("Should get location by user and latitude and longitude")
    void whenSaveLocationAndGetByUserAndLatitudeAndLongitudeThenReturnSavedLocation() {
        Optional<Location> foundLocation = locationRepository.getByUserAndLatitudeAndLongitude(
                user,
                location.getLatitude(),
                location.getLongitude()
        );
        assertThat(foundLocation).isPresent();
        assertThat(foundLocation.get().getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("Should get empty optional")
    void whenDontSaveLocationAndGetByUserAndLatitudeAndLongitudeThenReturnEmptyOptional() {
        Optional<Location> foundLocation = locationRepository.getByUserAndLatitudeAndLongitude(
                user,
                11.111111,
                11.111111
        );
        assertThat(foundLocation).isEmpty();
    }

    @Test
    @DisplayName("Should delete saved location")
    void whenSaveLocationAndDeleteByUserAndNameLocationThenOk() {
        locationRepository.save(location);
        locationRepository.deleteLocationByNameAndUser(location.getName(), user.getId());
        Optional<Location> foundLocation = locationRepository.getByUserAndLatitudeAndLongitude(
                user,
                location.getLatitude(),
                location.getLongitude()
        );
        assertThat(foundLocation).isEmpty();
    }

    @Test
    @DisplayName("Should throw exception when save location with equals user and latitude and longitude")
    void whenSaveTwoLocationsWithEqualsUserAndLatitudeAndLongitudeThenThrownException() {
        Location location2 = new Location();
        location2.setUser(user);
        location2.setName("gomel");
        location2.setLatitude(52.424160);
        location2.setLongitude(31.014281);
        locationRepository.save(location);
        assertThatThrownBy(() -> {
            entityManager.persistAndFlush(location2);
        }).isInstanceOf(PersistenceException.class);
        entityManager.clear();
    }

    @Test
    @DisplayName("Should get saved location")
    void whenSaveTwoLocationsWithEqualsLatitudeAndLongitudeThenOk() {
        User user2 = new User();
        user2.setLogin("vova");
        user2.setPassword("444");
        userRepository.save(user2);
        Location location2 = new Location();
        location2.setUser(user2);
        location2.setName("almost_gomel");
        location2.setLatitude(52.424160);
        location2.setLongitude(31.014281);
        locationRepository.save(location2);
        assertThat(locationRepository.getLocationsByUser(user2))
                .isNotEmpty()
                .hasSize(1)
                .containsExactlyInAnyOrder(
                        location2
                );
    }

    @Test
    @DisplayName("Should get save location")
    void whenSaveTwoLocationsWithEqualsNameThenOk() {
        Location location2 = new Location();
        location2.setUser(user);
        location2.setName("gomel");
        location2.setLatitude(52.111111);
        location2.setLongitude(31.111111);
        locationRepository.save(location2);
        assertThat(locationRepository.getLocationsByUser(user))
                .isNotEmpty()
                .hasSize(2)
                .containsExactlyInAnyOrder(
                        location, location2
                );
    }

    @Test
    @DisplayName("Should get save location")
    void whenSaveTwoLocationsWithEqualsUserThenOk() {
        Location location2 = new Location();
        location2.setUser(user);
        location2.setName("almost_gomel");
        location2.setLatitude(52.111111);
        location2.setLongitude(31.111111);
        locationRepository.save(location2);
        assertThat(locationRepository.getLocationsByUser(user))
                .isNotEmpty()
                .hasSize(2)
                .containsExactlyInAnyOrder(
                        location, location2
                );
    }
}