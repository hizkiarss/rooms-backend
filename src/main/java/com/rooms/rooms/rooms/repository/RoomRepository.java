package com.rooms.rooms.rooms.repository;

import com.rooms.rooms.rooms.entity.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Rooms, Long> {
    List<Rooms> getRoomsByPropertiesId(Long id);

    @Query("SELECT MAX(CAST(r.roomNumber AS integer)) FROM Rooms r WHERE r.properties.id = :propertyId")
    Integer findHighestRoomNumberByPropertyId(@Param("propertyId") Long propertyId);

    Optional<List<Rooms>> getRoomsByNameAndPropertiesId(String name, Long id);


    @Query("SELECT r FROM Rooms r " +
            "WHERE r.id NOT IN (" +
            "    SELECT DISTINCT b.room.id FROM Booking b " +
            "    WHERE b.room.properties.id = :propertyId " +
            "    AND (:checkInDate < b.endDate AND :checkOutDate > b.startDate)" +
            ") " +
            "AND r.isAvailable = true " +
            "AND r.properties.id = :propertyId")
    List<Rooms> findAvailableRooms(
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate,
            @Param("propertyId") Long propertyId);

    Integer countByIsAvailableTrueAndDeletedAtIsNullAndProperties_Id(Long propertyId);

}
