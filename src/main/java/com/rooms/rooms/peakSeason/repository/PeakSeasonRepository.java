package com.rooms.rooms.peakSeason.repository;

import com.rooms.rooms.peakSeason.entity.PeakSeason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PeakSeasonRepository extends JpaRepository<PeakSeason, Long> {
    List<PeakSeason> findByPropertiesId(Long id);

    @Query("SELECT ps FROM PeakSeason ps WHERE ps.properties.id IN :propertyId " +
            "AND :checkInDate BETWEEN ps.startDate AND ps.endDate")
    List<PeakSeason> findMarkedUpRooms(
            @Param("propertyId") Long propertyId,
            @Param("checkInDate") LocalDate checkInDate);

    @Query("SELECT ps FROM PeakSeason ps WHERE ps.endDate < :currentDate")
    List<PeakSeason> findEndedPeakSeasons(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT p FROM PeakSeason p WHERE p.properties.id = :propertyId AND :startDate BETWEEN p.startDate AND p.endDate AND p.deletedAt IS NULL")
    Optional<PeakSeason> findPeakSeasonByPropertyIdAndStartDate(
            @Param("propertyId") Long propertyId,
            @Param("startDate") LocalDate startDate);
}
