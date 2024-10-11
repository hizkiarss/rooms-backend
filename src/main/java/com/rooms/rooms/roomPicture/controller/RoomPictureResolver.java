package com.rooms.rooms.roomPicture.controller;


import com.rooms.rooms.roomPicture.dto.CreateRoomPictureDto;
import com.rooms.rooms.roomPicture.entity.RoomPicture;
import com.rooms.rooms.roomPicture.service.RoomPictureService;
import com.rooms.rooms.rooms.dto.AddRoomsRequestDto;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class RoomPictureResolver {
    private final RoomPictureService roomPictureService;

    public RoomPictureResolver(RoomPictureService roomPictureService) {
        this.roomPictureService = roomPictureService;
    }

    @MutationMapping(value = "addRoomPictures")
    public String addRoomPictures(@Argument("input") CreateRoomPictureDto dto) {
        roomPictureService.addRoomPictures(dto);
        return "Picture added successfully";
    }

    @MutationMapping(value = "deleteRoomPicture")
    public String deleteRoomPicture(@Argument List<Long> roomPictureIds) {
        roomPictureService.deleteRoomPictures(roomPictureIds);
        return "Deleted successfully";
    }

    @MutationMapping(value = "addPicturesForSingleRoom")
    public String addPicturesForSingleRoom(@Argument Long roomId, @Argument List<String> imgUrls) {
        roomPictureService.addPicturesForSingleRoom(roomId, imgUrls);
        return "Picture added successfully";
    }
}
