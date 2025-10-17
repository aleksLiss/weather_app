package ru.aleks.weather.service;

import org.springframework.stereotype.Service;
import ru.aleks.weather.dto.LocationSendDto;
import ru.aleks.weather.model.Location;
import ru.aleks.weather.repository.LocationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Optional<Location> getLocationByName(String name) {
        return locationRepository.getByName(name);
    }

    public Optional<Location> getByCoordinates(double latitude, double longitude) {
        return locationRepository.getByCoordinates(latitude, longitude);
    }

    public Optional<Location> save(Location location) {
        return locationRepository.save(location);
    }

    public List<Location> getAllLocationsByUserId(int userId) {
        return locationRepository.getAllByUserId(userId);
    }

    public boolean deleteLocationByUserId(int userId, String name) {
        return locationRepository.deleteByUserIdAndLocationName(userId, name);
    }
}
