package com.rooms.rooms.rooms.repository;

import com.rooms.rooms.rooms.entity.Rooms;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

//    @Query("SELECT r FROM Rooms r " +
//            "WHERE r.id NOT IN (" +
//            "    SELECT DISTINCT b.room.id FROM Booking b " +
//            "    WHERE b.room.properties.id = :propertyId " +
//            "    AND (:checkInDate < b.endDate AND :checkOutDate > b.startDate)" +
//            "    AND b.deletedAt IS NULL" +
//            ") " +
//            "AND r.isAvailable = true " +
//            "AND r.properties.id = :propertyId " +
//            "ORDER BY r.price ASC ")
//    List<Rooms> findAvailableRooms(
//            @Param("checkInDate") LocalDate checkInDate,
//            @Param("checkOutDate") LocalDate checkOutDate,
//            @Param("propertyId") Long propertyId);

    @Query("SELECT r FROM Rooms r " +
            "WHERE r.id NOT IN (" +
            "    SELECT DISTINCT b.room.id FROM Booking b " +
            "    WHERE b.room.properties.id = :propertyId " +
            "    AND (b.startDate BETWEEN :checkInDate AND :checkOutDate " +
            "    OR b.endDate BETWEEN :checkInDate AND :checkOutDate " +
            "    OR :checkInDate BETWEEN b.startDate AND b.endDate " +
            "    OR :checkOutDate BETWEEN b.startDate AND b.endDate) " +
            "    AND b.deletedAt IS NULL" +
            ") " +
            "AND r.isAvailable = true " +
            "AND r.properties.id = :propertyId " +
            "ORDER BY r.price ASC")
    List<Rooms> findAvailableRooms(
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate,
            @Param("propertyId") Long propertyId);


    @Query("SELECT r FROM Rooms r " +
            "WHERE r.properties.id = :propertyId" +
            "  AND (:isAvailable IS NULL OR r.isAvailable = :isAvailable)" +
            "  AND (:roomName IS NULL OR r.name LIKE :roomName)" +
            "ORDER BY r.name ASC")
    Page<Rooms> findFilteredRoomsByPropertySlug(
            @Param("propertyId") Long propertyId,
            @Param("isAvailable") Boolean isAvailable,
            @Param("roomName") String roomName,
            Pageable pageable);

    @Query(value = "SELECT r FROM Rooms r WHERE r.isAvailable = true ORDER BY RANDOM() LIMIT 10")
    List<Rooms> findRandomAvailableRooms();


    List<Rooms> getRoomsByPropertiesSlug(String propertySlug);


//     @Query("SELECT r FROM Rooms r " +
//             "WHERE r.id NOT IN (" +
//             "    SELECT DISTINCT b.room.id FROM Booking b " +
//             "    WHERE b.room.properties.id = :propertyId " +
//             "    AND (:checkInDate < b.endDate AND :checkOutDate > b.startDate)" +
//             "    AND b.deletedAt IS NULL" +
//             ") " +
//             "AND r.isAvailable = true " +
//             "AND r.properties.id = :propertyId"+
//             "ORDER BY r.price ASC ")
//     List<Rooms> findAvailableRooms(
//             @Param("checkInDate") LocalDate checkInDate,
//             @Param("checkOutDate") LocalDate checkOutDate,
//             @Param("propertyId") Long propertyId);

    Integer countByIsAvailableTrueAndDeletedAtIsNullAndProperties_Id(Long propertyId);

    @Query("SELECT COUNT(r) FROM Rooms r " +
            "JOIN Booking b ON r.id = b.room.id " +
            "WHERE r.properties.id = :propertyId " +
            "AND b.startDate <= :currentDate " +
            "AND b.endDate >= :currentDate " +
            "AND r.deletedAt IS NULL " +
            "AND b.deletedAt IS NULL")
    Integer countCurrentlyOccupiedRooms(
            @Param("propertyId") Long propertyId,
            @Param("currentDate") LocalDate currentDate);

    @Query("SELECT r.name " +
            "FROM Booking b " +
            "JOIN b.room r " +
            "WHERE r.properties.id = :propertyId " +
            "AND b.deletedAt IS NULL " +
            "GROUP BY r.name " +
            "ORDER BY COUNT(b.id) DESC")
    List<String> findTop5RoomNamesByBookingCountAndPropertyId(@Param("propertyId") Long propertyId);

    Rooms findBySlug(String slug);
}
