package com.rooms.rooms.rooms.service;

import com.rooms.rooms.rooms.dto.AddRoomsRequestDto;
import com.rooms.rooms.rooms.dto.DailyRoomPrice;
import com.rooms.rooms.rooms.dto.PagedRoomResult;
import com.rooms.rooms.rooms.dto.UpdateRoomRequestDto;
import com.rooms.rooms.rooms.entity.Rooms;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface RoomsService {
     Rooms getRoomsById(Long id);

     List<Rooms> getRoomsByPropertyId(Long propertyId);

     String createRoom(AddRoomsRequestDto dto, String email);

     Rooms updateRooms(Long id, UpdateRoomRequestDto dto, String email);

     void deleteRoom(Long id, String email);

     List<Rooms> updateRoomByName(String name, UpdateRoomRequestDto dto, String email, Long propertyId);

     List<Rooms> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, Long propertyId);

     List<Rooms> getAllAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, Long propertyId);

     void addSlug();

     List<DailyRoomPrice> getLowestRoomPricesForMonth(int year, int month, Long propertyId);

     List<String> getRoomsTypeByPropertyId(Long propertyId);

     Rooms saveRoom(Rooms rooms);

     Integer getTotalRooms(Long propertyId);

     Integer getOccupiedRooms(Long propertyId);

     Rooms getRandomRoomByName(List<Rooms> availableRooms, String roomName);

     List<String> getMostBookedRoomNames(Long propertyId);

     PagedRoomResult getFilteredRoomsByPropertyId(Long propertyId, Boolean isAvailable, String roomName, int pageSize, int pageNumber);

     List<Rooms> getRoomsByNameAndPropertyId(String roomName, Long propertyId);
     Rooms getRoomsBySlug(String slug);
     Float getRoomPrice(String slug, Long propertyId,  LocalDate checkInDate);
}
