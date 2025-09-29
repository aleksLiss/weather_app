package ru.aleks.weather.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.aleks.weather.mapper.LocationMapper;
import ru.aleks.weather.model.Location;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcLocationRepository implements LocationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final LocationMapper locationMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcLocationRepository.class);

    public JdbcLocationRepository(JdbcTemplate jdbcTemplate, LocationMapper locationMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.locationMapper = locationMapper;
    }

    @Override
    public Optional<Location> save(Location location) {
        String sql = "INSERT INTO locations(name, user_id, latitude, longitude) VALUES(?, ?, ?, ?)";
        Optional<Location> savedLocation;
        try {
            jdbcTemplate.update(sql,
                    location.getName(),
                    location.getUserId(),
                    location.getLatitude(),
                    location.getLongitude());
            savedLocation = Optional.ofNullable(
                    jdbcTemplate.queryForObject("SELECT * FROM locations WHERE name = ?",
                            new Object[]{location.getName()},
                            locationMapper)
            );
            LOGGER.info("LocationRepository: location was saved successfully");
        } catch (Exception ex) {
            LOGGER.warn("LocationRepository: location was not saved");
            throw new RuntimeException("error save location");
        }
        return savedLocation;
    }

    @Override
    public Optional<Location> getByName(String name) {
        String sql = "SELECT * FROM locations WHERE name = ?";
        Optional<Location> foundLocation;
        try {
            foundLocation = Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            sql,
                            new Object[]{name},
                            locationMapper
                    )
            );
            LOGGER.info("LocationRepository: location by name was found");
        } catch (Exception ex) {
            LOGGER.warn("LocationRepository: location by name was not found");
            throw new RuntimeException("error found location by name");
        }
        return foundLocation;
    }

    @Override
    public Optional<Location> getById(int id) {
        String sql = "SELECT * FROM locations WHERE id = ?";
        Optional<Location> foundLocation;
        try {
            foundLocation = Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            sql,
                            new Object[]{id},
                            locationMapper
                    )
            );
            LOGGER.info("LocationRepository: location by id was found");
        } catch (Exception ex) {
            LOGGER.warn("LocationRepository: location by id was not found");
            throw new RuntimeException("error found location by id");
        }
        return foundLocation;
    }

    @Override
    public List<Location> getAllByUserId(int userId) {
        String sql = "SELECT * FROM locations WHERE user_id = ?";
        List<Location> foundLocationsByUserId;
        try {
            foundLocationsByUserId = jdbcTemplate.queryForList(
                    sql,
                    Location.class,
                    userId
            );
            LOGGER.info("LocationRepository: locations by user id was found");
        } catch (Exception ex) {
            LOGGER.warn("LocationRepository: locations by user id was not found");
            throw new RuntimeException("error get all locations by user id");
        }
        return foundLocationsByUserId;
    }

    @Override
    public boolean deleteByUserId(int userId) {
        int deleted;
        String sql = "DELETE FROM locations WHERE user_id = ?";
        try {
            deleted = jdbcTemplate.update(sql, userId);
            LOGGER.info("LocationRepository: location by user id was deleted");
        } catch (Exception ex) {
            LOGGER.warn("LocationRepository: location by user id was not deleted");
            throw new RuntimeException("error delete location by user id");
        }
        return deleted != 0;
    }

    @Override
    public boolean updateByUserId(int userId, double newLatitude, double newLongitude) {
        int updated;
        String sql = "UPDATE locations SET latitude = ?, longitude = ? WHERE user_id = ?";
        try {
            updated = jdbcTemplate.update(sql, newLatitude, newLongitude, userId);
            LOGGER.info("LocationRepository: location by user id was updated");
        } catch (Exception ex) {
            LOGGER.warn("LocationRepository: location by user id was not updated");
            throw new RuntimeException("error update location by user id");
        }
        return updated != 0;
    }
}
