package com.rooms.rooms.rooms.controller;

import com.rooms.rooms.rooms.dto.AddRoomsRequestDto;
import com.rooms.rooms.rooms.dto.DailyRoomPrice;
import com.rooms.rooms.rooms.dto.UpdateRoomRequestDto;
import com.rooms.rooms.rooms.entity.Rooms;
import com.rooms.rooms.rooms.service.RoomsService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
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


    @MutationMapping(value = "addRoomsSlug")
    public String addSlug(){
    roomsService.addSlug();
    return "slug added for rooms";
    }

    @MutationMapping(value = "createRoom")
    public String createRoom(@Argument("input") AddRoomsRequestDto dto, @Argument String email) {
        roomsService.createRoom(dto, email);
        return "You have successfully created a new room";
    }

    @MutationMapping(value = "deleteRoom")
    public String deleteRoom(@Argument Long id, @Argument String email) {
        roomsService.deleteRoom(id, email);
        return "You have successfully deleted the room";
    }

    @MutationMapping(value = "updateRoom")
    public Rooms updateRoom(@Argument Long id, @Argument("input") UpdateRoomRequestDto dto, @Argument String email) {
        return roomsService.updateRooms(id, dto, email);
    }

    @MutationMapping(value = "updateRoomsByName")
    public List<Rooms> updateRoomsByName(@Argument String name, @Argument("input") UpdateRoomRequestDto dto, @Argument String email, @Argument Long propertyId) {
        return roomsService.updateRoomByName(name, dto, email, propertyId);
    }

    @QueryMapping(value ="getCalendarPrice")
    public List<DailyRoomPrice> getCalendarPrice (@Argument int year, @Argument int month, @Argument Long propertyId){
        return roomsService.getLowestRoomPricesForMonth(year, month, propertyId);
    }
}
