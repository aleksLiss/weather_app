package ru.aleks.weather.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
        Optional<Location> savedLocation = Optional.empty();
        try {
            jdbcTemplate.update(
                    sql,
                    location.getName(),
                    location.getUserId(),
                    location.getLatitude(),
                    location.getLongitude()
            );
            savedLocation = Optional.ofNullable(
                    jdbcTemplate
                            .queryForObject(
                                    "SELECT * FROM locations WHERE NAME = ?", new Object[]{location.getName()}, locationMapper
                            ));
            LOGGER.info("LocationRepository: Location was saved");
        } catch (DuplicateKeyException ex) {
            LOGGER.warn("LocationRepository: Location was not saved");
            throw new RuntimeException("location was not saved");
        }
        return savedLocation;
    }

    @Override
    public Optional<Location> getByName(String name) {
        String sql = "SELECT * FROM locations WHERE NAME = ?";
        Optional<Location> foundLocation = Optional.empty();
        try {
            foundLocation = Optional.ofNullable(
                    jdbcTemplate
                            .queryForObject(
                                    sql,
                                    new Object[]{name},
                                    locationMapper
                            ));
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
            foundLocationsByUserId = jdbcTemplate.query(
                    sql,
                    new BeanPropertyRowMapper<>(Location.class),
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
    public boolean deleteByUserIdAndLocationName(int userId, String name) {
        int deleted;
        String sql = "DELETE FROM locations WHERE user_id = ? AND name = ?";
        try {
            deleted = jdbcTemplate.update(sql, userId, name);
            LOGGER.info("LocationRepository: location by user id was deleted");
        } catch (Exception ex) {
            LOGGER.warn("LocationRepository: location by user id was not deleted");
            throw new RuntimeException("error delete location by user id");
        }
        return deleted != 0;
    }

    @Override
    public Optional<Location> getByCoordinates(double latitude, double longitude) {
        String sql = "SELECT * FROM locations WHERE latitude = ? AND longitude = ?";
        Optional<Location> foundLocation = Optional.empty();
        try {
            foundLocation = Optional.ofNullable(
                    jdbcTemplate
                            .queryForObject(
                                    sql,
                                    new Object[]{latitude, longitude},
                                    locationMapper
                            ));
            LOGGER.info("LocationRepository: location by name was found");
        } catch (Exception ex) {
            LOGGER.warn("LocationRepository: location by name was not found");
            throw new RuntimeException("error found location by name");
        }
        return foundLocation;

    }
}
