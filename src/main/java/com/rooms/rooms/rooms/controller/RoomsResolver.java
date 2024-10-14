package com.rooms.rooms.rooms.controller;

import com.rooms.rooms.rooms.dto.AddRoomsRequestDto;
import com.rooms.rooms.rooms.dto.DailyRoomPrice;
import com.rooms.rooms.rooms.dto.PagedRoomResult;
import com.rooms.rooms.rooms.dto.UpdateRoomRequestDto;
import com.rooms.rooms.rooms.entity.Rooms;
import com.rooms.rooms.rooms.service.RoomsService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;

@Controller
public class RoomsResolver {

    private final RoomsService roomsService;

    public RoomsResolver(RoomsService roomsService) {
        this.roomsService = roomsService;
    }

    @QueryMapping(value = "getRoomsById")
    public Rooms getRoomsById(@Argument Long id) {
        return roomsService.getRoomsById(id);
    }

    @QueryMapping(value = "getAvailableRooms")
    public List<Rooms> getAvailableRooms(@Argument LocalDate checkinDate, @Argument LocalDate checkOutDate, @Argument Long propertyId) {
        return roomsService.getAvailableRooms(checkinDate, checkOutDate, propertyId);
    }

    @QueryMapping(value = "getRoomsByPropertiesId")
    public List<Rooms> getRoomsByPropertiesId(@Argument Long id) {
        return roomsService.getRoomsByPropertyId(id);
    }

    @QueryMapping(value = "getFilteredRoomsByPropertyId")
    public PagedRoomResult getFilteredRoomsByPropertyId(@Argument Long propertyId, @Argument Boolean isAvailable, @Argument String roomName, @Argument int pageSize, @Argument int pageNumber) {
        return roomsService.getFilteredRoomsByPropertyId(propertyId, isAvailable, roomName, pageSize, pageNumber);
    }

    @QueryMapping(value = "getRoomsTypesByPropertyId")
    public List<String> getRoomsTypesByPropertyId(@Argument Long propertyId) {
        return roomsService.getRoomsTypeByPropertyId(propertyId);
    }

    @MutationMapping(value = "addRoomsSlug")
    public String addSlug() {
        roomsService.addSlug();
        return "slug added for rooms";
    }

    @PreAuthorize("hasAuthority('SCOPE_TENANT')")
    @MutationMapping(value = "createRoom")
    public String createRoom(@Argument("input") AddRoomsRequestDto dto, @Argument String email) {
        return roomsService.createRoom(dto, email);
    }

    @PreAuthorize("hasAuthority('SCOPE_TENANT')")
    @MutationMapping(value = "deleteRoom")
    public String deleteRoom(@Argument Long id, @Argument String email) {
        roomsService.deleteRoom(id, email);
        return "You have successfully deleted the room";
    }

    @PreAuthorize("hasAuthority('SCOPE_TENANT')")
    @MutationMapping(value = "updateRoom")
    public Rooms updateRoom(@Argument Long id, @Argument("input") UpdateRoomRequestDto dto, @Argument String email) {
        return roomsService.updateRooms(id, dto, email);
    }

    @PreAuthorize("hasAuthority('SCOPE_TENANT')")
    @MutationMapping(value = "updateRoomsByName")
    public List<Rooms> updateRoomsByName(@Argument String name, @Argument("input") UpdateRoomRequestDto dto, @Argument String email, @Argument Long propertyId) {
        return roomsService.updateRoomByName(name, dto, email, propertyId);
    }

    @QueryMapping(value = "getCalendarPrice")
    public List<DailyRoomPrice> getCalendarPrice(@Argument int year, @Argument int month, @Argument Long propertyId) {
        return roomsService.getLowestRoomPricesForMonth(year, month, propertyId);
    }

    @PreAuthorize("hasAuthority('SCOPE_TENANT')")
    @QueryMapping(value = "totalRoom")
    public Integer getTotalRoom(@Argument Long propertyId) {
        return roomsService.getTotalRooms(propertyId);
    }

    @PreAuthorize("hasAuthority('SCOPE_TENANT')")
    @QueryMapping(value = "occupiedRooms")
    public Integer getOccupiedRooms(@Argument Long propertyId) {
        return roomsService.getOccupiedRooms(propertyId);
    }

    @PreAuthorize("hasAuthority('SCOPE_TENANT')")
    @QueryMapping(value = "mostBookedRoomNames")
    public List<String> getMostBookedRoomNames(@Argument Long propertyId) {
        return roomsService.getMostBookedRoomNames(propertyId);
    }

    @QueryMapping("roomBySlug")
    public Rooms getRoomBySlug(@Argument String slug) {
        return roomsService.getRoomsBySlug(slug);
    }

    @QueryMapping("roomPrice")
    public Float getRoomPrice(@Argument String slug, @Argument Long propertyId, @Argument LocalDate checkinDate) {
        return roomsService.getRoomPrice(slug, propertyId, checkinDate);
    }

    @PreAuthorize("hasAuthority('SCOPE_TENANT')")
    @MutationMapping("setAvailable")
    public String setAvailable(@Argument Long roomId) {
        roomsService.setAvailable(roomId);
        return "room is available";
    }

    @PreAuthorize("hasAuthority('SCOPE_TENANT')")
    @MutationMapping("setUnavailable")
    public String setUnavailable(@Argument Long roomId) {
        roomsService.setUnavailable(roomId);
        return "room is unavailable";
    }

    @QueryMapping(value = "get10RandomAvailableRooms")
    public List<Rooms> get10RandomAvailableRooms (){
       return roomsService.get10RandomAvailableRooms();
    }

}
