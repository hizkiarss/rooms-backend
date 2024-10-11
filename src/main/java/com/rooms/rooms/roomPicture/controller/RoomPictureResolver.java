package com.rooms.rooms.roomPicture.controller;


import com.rooms.rooms.roomPicture.dto.CreateRoomPictureDto;
import com.rooms.rooms.roomPicture.entity.RoomPicture;
import com.rooms.rooms.roomPicture.service.RoomPictureService;
import com.rooms.rooms.rooms.dto.AddRoomsRequestDto;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class RoomPictureResolver {
    private final RoomPictureService roomPictureService;

    public RoomPictureResolver(RoomPictureService roomPictureService) {
        this.roomPictureService = roomPictureService;
    }

    @MutationMapping (value = "addRoomPictures")
    public String addRoomPictures(@Argument("input") CreateRoomPictureDto dto) {
         roomPictureService.addRoomPictures(dto);
        return "Picture added successfully";
    }
}
