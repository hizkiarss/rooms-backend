package com.rooms.rooms.rooms.service.impl;

import com.rooms.rooms.exceptions.DataNotFoundException;
import com.rooms.rooms.rooms.entity.Rooms;
import com.rooms.rooms.rooms.repository.RoomRepository;
import com.rooms.rooms.rooms.service.RoomsService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoomsServiceImpl implements RoomsService {
     private RoomRepository roomRepository;
     public RoomsServiceImpl(RoomRepository roomRepository) {
          this.roomRepository = roomRepository;
     }
     @Override
     @Cacheable(value = "getRoomsById", key = "#id")
     public Rooms getRoomsById(Long id) {
          Optional<Rooms> room = roomRepository.findById(id);
          if(room.isEmpty() || room == null){
               throw new DataNotFoundException("Room with id " + id + " not found");
          }
          return room.orElse(null);
     }
}
