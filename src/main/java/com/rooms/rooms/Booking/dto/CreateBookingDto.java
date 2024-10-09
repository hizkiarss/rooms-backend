package com.rooms.rooms.Booking.dto;

import com.rooms.rooms.Booking.Entity.Booking;
import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.rooms.entity.Rooms;
import com.rooms.rooms.transactionDetail.entity.TransactionDetail;
import com.rooms.rooms.users.entity.Users;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateBookingDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private Long propertyId;
    private Long userId;
    private Long roomId;
    private Long transactionDetailId;

    public Booking toEntity(Properties properties, Users users, Rooms rooms, TransactionDetail transactionDetail) {
        Booking booking = new Booking();
        booking.setStartDate(this.startDate);
        booking.setEndDate(this.endDate);
        booking.setProperty(properties);
        booking.setUsers(users);
        booking.setRoom(rooms);
        booking.setTransactionDetail(transactionDetail);
        return booking;
    }
}
