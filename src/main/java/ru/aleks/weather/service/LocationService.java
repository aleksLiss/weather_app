package ru.aleks.weather.service;

import org.springframework.stereotype.Service;
import ru.aleks.weather.model.Location;
import ru.aleks.weather.repository.LocationRepository;

import java.util.Optional;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Optional<Location> getLocationByName(String name) {
        // watch again ! https://www.youtube.com/watch?v=syjOb_jPJWE&t=813s
        return locationRepository.getByName(name);
    }

    public Optional<Location> getByCoordinates(double latitude, double longitude) {
        // check: latitude = from -90 to 90, longitude = from -180 to 180
        return locationRepository.getByCoordinates(latitude, longitude);
    }
}
