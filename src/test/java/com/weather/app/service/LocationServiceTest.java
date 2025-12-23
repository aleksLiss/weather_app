package com.weather.app.service;

import com.weather.app.dto.FoundLocationDto;
import com.weather.app.exception.LocationAlreadyExistsException;
import com.weather.app.model.Location;
import com.weather.app.model.User;
import com.weather.app.repository.LocationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private User user;
    @Mock
    private Location location;
    @InjectMocks
    private LocationService locationService;

    @Test
    void updateLocationThenOk() {
        double latitude = 22.222111;
        double longitude = 22.222333;
        doNothing().when(locationRepository)
                .updateLocation(latitude, longitude);
        locationService.updateLocation(latitude, longitude);
        verify(locationRepository, times(1))
                .updateLocation(latitude, longitude);
    }

    @Test
    void whenSaveLocationAndLocationAlreadyExistsThenThrowException() {
        FoundLocationDto dto = new FoundLocationDto();
        dto.setName("London");
        dto.setCountry("GB");
        User user = new User();
        when(locationRepository.save(any(Location.class)))
                .thenThrow(RuntimeException.class);
        assertThatThrownBy(() -> locationService.save(dto, user))
                .isInstanceOf(LocationAlreadyExistsException.class);
    }

    @Test
    void whenSaveLocationThenOk() {
        when(locationRepository.save(any(Location.class))).thenReturn(location);
        Location savedLocation = locationRepository.save(location);
        assertThat(savedLocation).isNotNull();
        assertThat(savedLocation).isEqualTo(location);
    }

    @Test
    void whenGetLocationByUserAndLatitudeAndLongitudeThenReturnSavedLocation() {
        when(locationRepository.save(any(Location.class))).thenReturn(location);
        FoundLocationDto foundLocationDto = new FoundLocationDto();
        foundLocationDto.setName("name");
        foundLocationDto.setCountry("country");
        locationService.save(foundLocationDto, user);
        when(locationRepository
                .getByUserAndLatitudeAndLongitude(any(User.class), anyDouble(), anyDouble()))
                .thenReturn(Optional.of(location));
        Optional<Location> savedLocation = locationService.getLocationByUserAndLatitudeAndLongitude(
                user,
                location.getLatitude(),
                location.getLongitude()
        );
        assertThat(savedLocation).isPresent();
        assertThat(savedLocation.get().getName()).isEqualTo(location.getName());
    }

    @Test
    void whenDontSaveLocationAndGetItByUserAndLatitudeAndLongitudeThenReturnEmptyOptional() {
        Optional<Location> savedLocation = locationService.getLocationByUserAndLatitudeAndLongitude(
                user,
                location.getLatitude(),
                location.getLongitude()
        );
        assertThat(savedLocation).isEmpty();
    }

    @Test
    void whenSaveSomeLocationsAndGetItByUserThenReturnListOfSavedLocations() {
        when(locationRepository.save(any(Location.class))).thenReturn(location);
        FoundLocationDto foundLocationDto = new FoundLocationDto();
        foundLocationDto.setName("name");
        foundLocationDto.setCountry("country");
        locationService.save(foundLocationDto, user);
        when(locationRepository.getLocationsByUser(any(User.class))).thenReturn(
                List.of(location)
        );
        List<Location> locations = locationService.getLocationByUser(user);
        assertThat(locations)
                .isNotEmpty()
                .hasSize(1)
                .containsExactlyInAnyOrder(
                        location
                );
    }

    @Test
    void whenDontSavedSomeLocationsAndGetItByUserThenReturnEmptyList() {
        when(locationRepository.getLocationsByUser(any(User.class))).thenReturn(
                List.of()
        );
        List<Location> locations = locationService.getLocationByUser(user);
        assertThat(locations).isEmpty();
    }

    @Test
    void whenSaveLocationAndDeleteItThenOk() {
        String city = "moscow";
        int userId = 1;
        doNothing()
                .when(locationRepository)
                .deleteLocationByNameAndUser(any(String.class), anyInt());
        locationService.deleteLocationByLocationNameAndUserId(
                city, userId
        );
        verify(locationRepository, times(1))
                .deleteLocationByNameAndUser(eq(city), eq(userId));
    }
}