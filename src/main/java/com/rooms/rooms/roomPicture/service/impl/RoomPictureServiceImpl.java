package com.rooms.rooms.roomPicture.service.impl;

import com.rooms.rooms.roomPicture.dto.CreateRoomPictureDto;
import com.rooms.rooms.roomPicture.entity.RoomPicture;
import com.rooms.rooms.roomPicture.repository.RoomPictureRepository;
import com.rooms.rooms.roomPicture.service.RoomPictureService;
import com.rooms.rooms.rooms.entity.Rooms;
import com.rooms.rooms.rooms.service.RoomsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomPictureServiceImpl implements RoomPictureService {
    private final RoomPictureRepository roomPictureRepository;
    private final RoomsService roomsService;

    public RoomPictureServiceImpl(RoomPictureRepository roomPictureRepository, RoomsService roomsService) {
        this.roomPictureRepository = roomPictureRepository;
        this.roomsService = roomsService;
    }

    @Override
    public void addRoomPictures(CreateRoomPictureDto dto) {
        List<Rooms> roomsList = roomsService.getRoomsByNameAndPropertyId(dto.getRoomName(), dto.getPropertyId());
        for (Rooms room : roomsList) {
            for (String imgUrl : dto.getRoomPicture()) {
                RoomPicture roomPicture = new RoomPicture();
                roomPicture.setRooms(room);
                roomPicture.setImgUrl(imgUrl);
                roomPictureRepository.save(roomPicture);
            }
        }
    }


}
