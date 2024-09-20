package com.rooms.rooms.Booking.Repository;

import com.rooms.rooms.Booking.Entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> getIsBookedsByPropertyId(Long propertyId);

}
