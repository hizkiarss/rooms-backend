package com.rooms.rooms.Booking.Controller;

import com.rooms.rooms.Booking.Entity.Booking;
import com.rooms.rooms.Booking.Service.BookingService;
import com.rooms.rooms.Booking.dto.CreateBookingDto;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class BookingResolver {
    private final BookingService bookingService;

    public BookingResolver(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @MutationMapping(value = "createBooking")
    public Booking createBooking(@Argument("input") CreateBookingDto dto) {
        return bookingService.createBooking(dto);
    }

    @PreAuthorize("hasAuthority('SCOPE_TENANT')")
    @QueryMapping(value = "upcomingBookings")
    public List<Booking> getUpcomingBookingsByPropertyId(@Argument Long propertyId) {
        return bookingService.getUpcomingBookingsByPropertyId(propertyId);
    }
}
