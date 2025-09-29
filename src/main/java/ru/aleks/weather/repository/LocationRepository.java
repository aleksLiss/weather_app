package ru.aleks.weather.repository;

import ru.aleks.weather.model.Location;

import java.util.List;
import java.util.Optional;

public interface LocationRepository {

    Optional<Location> save(Location location);

    Optional<Location> getByName(String name);

    Optional<Location> getById(int id);

    List<Location> getAllByUserId(int userId);

    boolean deleteByUserId(int userId);

    boolean updateByUserId(int userId, double newLatitude, double newLongitude);
}
