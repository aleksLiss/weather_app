package com.weather.app.repository;

import com.weather.app.model.Location;
import com.weather.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {

    Optional<Location> getByUserAndLatitudeAndLongitude(User user, double latitude, double longitude);

    List<Location> getLocationsByUser(User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM Location l WHERE l.name LIKE :name AND l.user.id=:userId")
    void deleteLocationByNameAndUser(@Param("name") String name, @Param("userId") int userId);

    @Modifying
    @Transactional
    @Query("UPDATE Location l SET l.latitude = :lat, l.longitude = :lon")
    void updateLocation(@Param("lat") double lat, @Param("lon") double lon);
}
