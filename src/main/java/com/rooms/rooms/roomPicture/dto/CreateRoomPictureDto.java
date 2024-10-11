package com.rooms.rooms.roomPicture.dto;

import lombok.Data;

import java.util.List;
@Data
public class CreateRoomPictureDto {
    private String roomName;
    private Long propertyId;
    private List<String> roomPicture;
}
