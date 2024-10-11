package com.rooms.rooms.roomPicture.service.impl;

import com.rooms.rooms.roomPicture.dto.CreateRoomPictureDto;
import com.rooms.rooms.roomPicture.entity.RoomPicture;
import com.rooms.rooms.roomPicture.repository.RoomPictureRepository;
import com.rooms.rooms.roomPicture.service.RoomPictureService;
import com.rooms.rooms.rooms.entity.Rooms;
import com.rooms.rooms.rooms.service.RoomsService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public void deleteRoomPictures(List<Long> roomPictureIds) {
        for (Long roomPictureId : roomPictureIds) {
            RoomPicture roomPicture = roomPictureRepository.findById(roomPictureId).orElse(null);
            roomPicture.setDeletedAt(Instant.now());
            roomPictureRepository.save(roomPicture);
        }
    }

    @Override
    public void addPicturesForSingleRoom(Long roomId, List<String> imgUrls) {
        Rooms room = roomsService.getRoomsById(roomId);

        List<RoomPicture> roomPictures = imgUrls.stream().map(imgUrl -> {
            RoomPicture roomPicture = new RoomPicture();
            roomPicture.setRooms(room);
            roomPicture.setImgUrl(imgUrl);
            return roomPicture;
        }).collect(Collectors.toList());

        roomPictureRepository.saveAll(roomPictures);
    }
}













