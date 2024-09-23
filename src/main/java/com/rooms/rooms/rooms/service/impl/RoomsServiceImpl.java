package com.rooms.rooms.rooms.service.impl;

import com.rooms.rooms.bedTypes.entity.BedTypes;
import com.rooms.rooms.bedTypes.service.BedTypesService;
import com.rooms.rooms.exceptions.DataNotFoundException;
import com.rooms.rooms.exceptions.UnauthorizedAccessException;
import com.rooms.rooms.peakSeason.entity.PeakSeason;
import com.rooms.rooms.peakSeason.service.PeakSeasonService;
import com.rooms.rooms.properties.dto.PropertyOwnerDto;
import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.properties.service.PropertiesService;
import com.rooms.rooms.rooms.dto.AddRoomsRequestDto;
import com.rooms.rooms.rooms.dto.UpdateRoomRequestDto;
import com.rooms.rooms.rooms.entity.Rooms;
import com.rooms.rooms.rooms.repository.RoomRepository;
import com.rooms.rooms.rooms.service.RoomsService;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomsServiceImpl implements RoomsService {
    private final RoomRepository roomRepository;
    private final PropertiesService propertiesService;
    private final BedTypesService bedTypesService;
    private final PeakSeasonService peakSeasonService;

    public RoomsServiceImpl(RoomRepository roomRepository, PropertiesService propertiesService, BedTypesService bedTypesService, @Lazy PeakSeasonService peakSeasonService) {
        this.roomRepository = roomRepository;
        this.propertiesService = propertiesService;
        this.bedTypesService = bedTypesService;
        this.peakSeasonService = peakSeasonService;
    }

    @Transactional
    @Override
//    @Cacheable(value = "getRoomsById", key = "#id")
    public Rooms getRoomsById(Long id) {
        Optional<Rooms> room = roomRepository.findById(id);


        if (room.isEmpty() || room == null) {
            throw new DataNotFoundException("Room with id " + id + " not found");
        }
        return room.orElse(null);
    }

    @Override
    public List<Rooms> getRoomsByPropertyId(Long propertyId) {
        return roomRepository.getRoomsByPropertiesId(propertyId);
    }

    @Transactional
    @Override
    public void createRoom(AddRoomsRequestDto dto, String email) {
        Properties properties = propertiesService.getPropertiesById(dto.getPropertyId());
        PropertyOwnerDto propertyOwner = propertiesService.getPropertyOwnerById(dto.getPropertyId());
        if (!propertyOwner.getEmail().equals(email)) {
            throw new UnauthorizedAccessException("You are not authorized to add rooms for this property");
        }
        BedTypes bedTypes = bedTypesService.getBedTypesByName(dto.getBedType());

        Integer highestRoomNumber = roomRepository.findHighestRoomNumberByPropertyId(dto.getPropertyId());
        int startingRoomNumber = (highestRoomNumber != null) ? highestRoomNumber + 1 : 1;
        for (int i = 0; i < dto.getNumberOfRooms(); i++) {
            String roomNumber = String.valueOf(startingRoomNumber + i);
            Rooms room = dto.toEntity(properties, bedTypes);
            room.setRoomNumber(roomNumber);
            roomRepository.save(room);
        }
    }

    @Override
    public Rooms updateRooms(Long id, UpdateRoomRequestDto dto, String email) {
        Rooms room = roomRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Room with id not found"));
        PropertyOwnerDto propertyOwner = propertiesService.getPropertyOwnerById(room.getProperties().getId());
        if (!propertyOwner.getEmail().equals(email)) {
            throw new UnauthorizedAccessException("You are not authorized to add rooms for this property");
        }
        BedTypes bedType = bedTypesService.getBedTypesByName(dto.getBedType());

        return roomRepository.save(dto.toEntity(room, bedType));
    }

    @Override
    public void deleteRoom(Long id, String email) {
        Rooms room = roomRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Room with id not found"));
        PropertyOwnerDto propertyOwner = propertiesService.getPropertyOwnerById(room.getProperties().getId());
        if (!propertyOwner.getEmail().equals(email)) {
            throw new UnauthorizedAccessException("You are not authorized to add rooms for this property");
        }
        room.setDeletedAt(Instant.now());
        roomRepository.save(room);
    }

    @Override
    public List<Rooms> updateRoomByName(String name, UpdateRoomRequestDto dto, String email, Long propertyId) {
        List<Rooms> rooms = roomRepository.getRoomsByNameAndPropertiesId(name, propertyId)
                .orElseThrow(() -> new DataNotFoundException("Room with name " + name + " not found in this property"));
        PropertyOwnerDto propertyOwner = propertiesService.getPropertyOwnerById(propertyId);
        if (!propertyOwner.getEmail().equals(email)) {
            throw new UnauthorizedAccessException("You are not authorized to add rooms for this property");
        }

        BedTypes bedType = new BedTypes();

        if (dto.getBedType() != null) {
            bedType = bedTypesService.getBedTypesByName(dto.getBedType());
        }

        BedTypes finalBedType = bedType;
        List<Rooms> updatedRooms = rooms.stream().map(room -> dto.toEntity(room, finalBedType)).toList();
        roomRepository.saveAll(updatedRooms);
        return updatedRooms;
    }

    @Override
    public List<Rooms> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, Long propertyId) {
        List<Rooms> availableRooms = roomRepository.findAvailableRooms(checkInDate, checkOutDate, propertyId);
        List<Long> availableRoomsIds = availableRooms.stream().map(Rooms::getId).toList();
        List<PeakSeason> markedUpRooms = peakSeasonService.findMarkedUpRooms(availableRoomsIds, checkInDate);

        if (markedUpRooms != null && !markedUpRooms.isEmpty()) {
            Map<Long, Double> roomMarkupMap = markedUpRooms.stream()
                    .collect(Collectors.toMap(
                            peakSeason -> peakSeason.getRoom().getId(),
                            PeakSeason::getMarkUpPercentage,
                            (existing, replacement) -> Math.max(existing, replacement)

                    ));

            availableRooms.forEach(room -> {
                if (roomMarkupMap.containsKey(room.getId())) {
                    double markupPercentage = roomMarkupMap.get(room.getId());
                    double originalPrice = room.getPrice();
                    double adjustedPrice = originalPrice * (1 + markupPercentage / 100);
                    room.setPrice(adjustedPrice);
                }
            });
        }

        return availableRooms;
    }

    @Override
    public Rooms saveRoom(Rooms rooms) {
        return roomRepository.save(rooms);
    }

    @Override
    public Integer getTotalRooms(Long propertyId){
        Properties properties = propertiesService.getPropertiesById(propertyId);
        return roomRepository.countByIsAvailableTrueAndDeletedAtIsNullAndProperties_Id(properties.getId());
    }
}


