package com.rooms.rooms.Booking.Service.impl;

import com.rooms.rooms.Booking.Entity.Booking;
import com.rooms.rooms.Booking.Repository.BookingRepository;
import com.rooms.rooms.Booking.Service.BookingService;
import com.rooms.rooms.Booking.dto.CreateBookingDto;
import com.rooms.rooms.exceptions.DataNotFoundException;
import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.properties.service.PropertiesService;
import com.rooms.rooms.rooms.entity.Rooms;
import com.rooms.rooms.rooms.service.RoomsService;
import com.rooms.rooms.transactionDetail.entity.TransactionDetail;
import com.rooms.rooms.transactionDetail.service.TransactionDetailService;
import com.rooms.rooms.users.entity.Users;
import com.rooms.rooms.users.service.UsersService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final PropertiesService propertiesService;
    private final UsersService usersService;
    private final RoomsService roomsService;
    private final TransactionDetailService transactionDetailService;

    public BookingServiceImpl(BookingRepository bookingRepository, PropertiesService propertiesService, UsersService usersService, RoomsService roomsService, TransactionDetailService transactionDetailService) {
        this.bookingRepository = bookingRepository;
        this.propertiesService = propertiesService;
        this.usersService = usersService;
        this.roomsService = roomsService;
        this.transactionDetailService = transactionDetailService;
    }

    @Override
    public List<Booking> getBookedPropertiesByPropertyID(Long propertyID) {
        return bookingRepository.getIsBookedsByPropertyId(propertyID);
    }

    @Override
    public Booking createBooking(CreateBookingDto dto) {

        Properties properties = propertiesService.getPropertiesById(dto.getPropertyId());
        Users user = usersService.getUsersById(dto.getUserId());
        Rooms room = roomsService.getRoomsById(dto.getRoomId());

        if (!Objects.equals(room.getProperties().getId(), properties.getId())) {
            throw new DataNotFoundException("There's no properties with id: " + properties.getId());
        }
        return bookingRepository.save(dto.toEntity(properties, user, room));
    }

    @Override
    public Booking getBookingByTransactionDetailId(Long transactionDetailId){
        TransactionDetail transactionDetail = transactionDetailService.getTransactionDetailById(transactionDetailId);
        return bookingRepository.findBookingByTransactionDetailIdAndDeletedAtIsNull(transactionDetail.getId());
    }

    @Override
    public void deleteBookingById(Long id){
        Optional<Booking> booking = bookingRepository.findById(id);
        if(booking.isEmpty() || booking == null) {
            throw new DataNotFoundException("Booking not found");
        }
        booking.get().setDeletedAt(Instant.now());
        bookingRepository.save(booking.get());
    }
}
