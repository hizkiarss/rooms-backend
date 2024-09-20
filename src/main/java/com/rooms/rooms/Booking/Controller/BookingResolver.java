package com.rooms.rooms.Booking.Controller;

import com.rooms.rooms.Booking.Entity.Booking;
import com.rooms.rooms.Booking.Service.BookingService;
import com.rooms.rooms.Booking.dto.CreateBookingDto;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

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
}
