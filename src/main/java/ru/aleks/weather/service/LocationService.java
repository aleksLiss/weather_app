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
        //check if saved location then return location from db
        // if not saved than save
        //
        //
        return locationRepository.getByName(name);
    }

    public Optional<Location> getByCoordinates(double latitude, double longitude) {
        return locationRepository.getByCoordinates(latitude, longitude);
    }
}
