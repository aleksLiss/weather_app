package com.weather.app.mapper;

import com.weather.app.dto.FoundLocationDto;
import com.weather.app.model.Location;
import com.weather.app.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FoundLocationDtoToLocationMapperTest {

    private FoundLocationDto foundLocationDto;
    private User user;
    private Location location;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setLogin("login");
        foundLocationDto = new FoundLocationDto();
        foundLocationDto.setName("name");
        foundLocationDto.setLat(11.111);
        foundLocationDto.setLon(22.222);
        location = new Location();
        location.setUser(user);
        location.setLatitude(foundLocationDto.getLat());
        location.setLongitude(foundLocationDto.getLon());
        location.setName(foundLocationDto.getName());
    }

    @Test
    void whenFoundLocationDtoAndUserIsPresentThenReturnLocation() {
        FoundLocationDtoToLocationMapper foundLocationDtoToLocationMapper = new FoundLocationDtoToLocationMapper();
        Location resultLocation = foundLocationDtoToLocationMapper.fromFoundLocationDtoToLocationBuilder(foundLocationDto, user);
        assertThat(resultLocation).isNotNull();
        assertThat(resultLocation.getUser()).isEqualTo(location.getUser());
        assertThat(resultLocation.getName()).isEqualTo(location.getName());
    }

    @Test
    void whenFoundLocationDtoAndUserIsNotPresentThenReturnLocationWithEmptyFields() {
        User newUser = new User();
        FoundLocationDto foundLocationDto = new FoundLocationDto();
        Location newLocation = new Location();
        newLocation.setUser(newUser);
        newLocation.setLatitude(foundLocationDto.getLat());
        newLocation.setLongitude(foundLocationDto.getLon());
        newLocation.setName(foundLocationDto.getName());
        FoundLocationDtoToLocationMapper foundLocationDtoToLocationMapper = new FoundLocationDtoToLocationMapper();
        Location resultLocation = foundLocationDtoToLocationMapper.fromFoundLocationDtoToLocationBuilder(foundLocationDto, newUser);
        assertThat(resultLocation).isNotNull();
        assertThat(resultLocation.getUser()).isEqualTo(newLocation.getUser());
        assertThat(resultLocation.getName()).isNull();
    }

}