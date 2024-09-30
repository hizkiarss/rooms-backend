package com.rooms.rooms.Booking.Repository;

import com.rooms.rooms.Booking.Entity.Booking;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
     List<Booking> getIsBookedsByPropertyId(Long propertyId);

     Booking findBookingByTransactionDetailIdAndDeletedAtIsNull(Long transactionDetailId);

     @Query("SELECT b " +
             "FROM Booking b " +
             "WHERE b.startDate >= :today " +
             "AND b.deletedAt IS NULL " +
             "AND b.property.id = :propertyId " +
             "ORDER BY b.startDate ASC")
     List<Booking> findUpcomingBookingsByPropertyId(@Param("today") LocalDate today, @Param("propertyId") Long propertyId);

}
