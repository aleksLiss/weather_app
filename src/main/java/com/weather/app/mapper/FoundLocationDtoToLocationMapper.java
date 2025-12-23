package com.weather.app.mapper;

import com.weather.app.dto.FoundLocationDto;
import com.weather.app.model.Location;
import com.weather.app.model.User;

public class FoundLocationDtoToLocationMapper {

    public Location fromFoundLocationDtoToLocationBuilder(FoundLocationDto foundLocationDto, User user) {
        return new Location(
                foundLocationDto.getName(),
                user,
                foundLocationDto.getLat(),
                foundLocationDto.getLon()
        );
    }
}
