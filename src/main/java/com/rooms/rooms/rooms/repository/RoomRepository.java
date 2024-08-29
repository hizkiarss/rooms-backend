package com.rooms.rooms.rooms.repository;

import com.rooms.rooms.rooms.entity.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Rooms, Long> {
}
