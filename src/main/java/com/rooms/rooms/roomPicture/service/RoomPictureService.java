package com.rooms.rooms.roomPicture.service;

import com.rooms.rooms.roomPicture.dto.CreateRoomPictureDto;

import java.util.List;

public interface RoomPictureService {
    void addRoomPictures(CreateRoomPictureDto dto);

    void deleteRoomPictures(List<Long> roomPictureIds);

    void addPicturesForSingleRoom(Long roomId, List<String> imgUrls);
}
