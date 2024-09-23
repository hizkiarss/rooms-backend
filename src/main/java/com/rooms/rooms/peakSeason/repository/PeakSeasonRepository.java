package com.rooms.rooms.peakSeason.repository;

import com.rooms.rooms.peakSeason.entity.PeakSeason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PeakSeasonRepository extends JpaRepository<PeakSeason, Long> {
    List<PeakSeason> findByPropertiesId(Long id);

    @Query("SELECT ps FROM PeakSeason ps WHERE ps.room.id IN :roomIds " +
            "AND :checkInDate BETWEEN ps.startDate AND ps.endDate")
    List<PeakSeason> findMarkedUpRooms(
            @Param("roomIds") List<Long> roomIds,
            @Param("checkInDate") LocalDate checkInDate);

    @Query("SELECT ps FROM PeakSeason ps WHERE ps.endDate < :currentDate")
    List<PeakSeason> findEndedPeakSeasons(@Param("currentDate") LocalDate currentDate);
}
