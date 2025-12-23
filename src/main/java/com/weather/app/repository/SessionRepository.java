package com.weather.app.repository;

import com.weather.app.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {

    @Query("SELECT s FROM Session s WHERE s.id = :id")
    Optional<Session> getSessionsByUUID(@Param("id") UUID uuid);

    @Modifying
    @Query("DELETE FROM Session s WHERE s.id = :id")
    void deleteSessionByUUID(@Param("id") UUID uuid);

    @Query("FROM Session s WHERE s.user.id = :userId ORDER BY s.expiresAt DESC")
    List<Session> findAllByUserId(@Param("userId") int userId);
}
