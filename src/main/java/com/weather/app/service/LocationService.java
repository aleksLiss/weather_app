package com.weather.app.service;

import com.weather.app.dto.FoundLocationDto;
import com.weather.app.exception.LocationAlreadyExistsException;
import com.weather.app.model.Location;
import com.weather.app.model.User;
import com.weather.app.repository.LocationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    private final LocationRepository locationRepository;


    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Optional<Location> save(FoundLocationDto foundLocationDto, User user) {
        Location location = new Location();
        String name = foundLocationDto.getCountry() + ", " + foundLocationDto.getName();
        location.setUser(user);
        location.setName(name);
        location.setLatitude(foundLocationDto.getLat());
        location.setLongitude(foundLocationDto.getLon());
        try {
            return Optional.of(locationRepository.save(location));
        } catch (Exception ex) {
            throw new LocationAlreadyExistsException("This location already exist");
        }
    }

    public Optional<Location> getLocationByUserAndLatitudeAndLongitude(User user, double latitude, double longitude) {
        return locationRepository.getByUserAndLatitudeAndLongitude(user, latitude, longitude);
    }

    public List<Location> getLocationByUser(User user) {
        return locationRepository.getLocationsByUser(user);
    }

    @Transactional
    public void deleteLocationByLocationNameAndUserId(String locationName, int userId) {
        locationRepository.deleteLocationByNameAndUser(locationName, userId);
    }

    @Transactional
    public void updateLocation(double latitude, double longitude) {
        locationRepository.updateLocation(latitude, longitude);

    }
}
