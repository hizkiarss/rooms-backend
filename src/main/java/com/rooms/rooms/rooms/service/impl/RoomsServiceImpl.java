package com.rooms.rooms.rooms.service.impl;

import com.rooms.rooms.bedTypes.entity.BedTypes;
import com.rooms.rooms.bedTypes.service.BedTypesService;
import com.rooms.rooms.exceptions.DataNotFoundException;
import com.rooms.rooms.exceptions.UnauthorizedAccessException;
import com.rooms.rooms.helper.SlugifyHelper;
import com.rooms.rooms.helper.StringGenerator;
import com.rooms.rooms.peakSeason.entity.PeakSeason;
import com.rooms.rooms.peakSeason.service.PeakSeasonService;
import com.rooms.rooms.properties.dto.PropertyOwnerDto;
import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.properties.service.PropertiesService;
import com.rooms.rooms.rooms.dto.AddRoomsRequestDto;
import com.rooms.rooms.rooms.dto.DailyRoomPrice;
import com.rooms.rooms.rooms.dto.PagedRoomResult;
import com.rooms.rooms.rooms.dto.UpdateRoomRequestDto;
import com.rooms.rooms.rooms.entity.Rooms;
import com.rooms.rooms.rooms.repository.RoomRepository;
import com.rooms.rooms.rooms.service.RoomsService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class RoomsServiceImpl implements RoomsService {
    private static final Logger log = LoggerFactory.getLogger(RoomsServiceImpl.class);
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

    @Override
    public PagedRoomResult getFilteredRoomsByPropertyId(Long propertyId, Boolean isAvailable, String roomName, int pageSize, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("roomNumber").ascending());
        Page<Rooms> roomsPageable = roomRepository.findFilteredRoomsByPropertySlug(propertyId, isAvailable, roomName, pageable);
        PagedRoomResult dto = new PagedRoomResult();
        dto.toDto(roomsPageable, dto);
        return dto;
    }

    @Transactional
    @Override
    public String createRoom(AddRoomsRequestDto dto, String email) {
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

        return dto.getName();

    }

    @Override
    public Rooms updateRooms(Long id, UpdateRoomRequestDto dto, String email) {
        Rooms room = roomRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Room with id not found"));
        PropertyOwnerDto propertyOwner = propertiesService.getPropertyOwnerById(room.getProperties().getId());
        if (!propertyOwner.getEmail().equals(email)) {
            throw new UnauthorizedAccessException("You are not authorized to add rooms for this property");
        }
        BedTypes bedType = bedTypesService.getBedTypesByName(dto.getBedType());

        return roomRepository.save(dto.toEntity(room, bedType, false));
    }

    @Override
    public void addSlug() {
        List<Rooms> allRooms = roomRepository.findAll();
        for (Rooms room : allRooms) {
            String slug = SlugifyHelper.slugify((room.getName()));
            String uniqueCode = StringGenerator.generateRandomString(4);
            room.setSlug(slug + "-" + uniqueCode);
            roomRepository.save(room);
        }
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
        List<Rooms> updatedRooms = rooms.stream().map(room -> dto.toEntity(room, finalBedType, true)).toList();
        roomRepository.saveAll(updatedRooms);
        return updatedRooms;
    }

    public Rooms clone(Rooms rooms) {
        Rooms clonedRoom = new Rooms();
        clonedRoom.setId(rooms.getId());
        clonedRoom.setName(rooms.getName());
        ;
        clonedRoom.setPrice(rooms.getPrice());
        return clonedRoom;
    }

     @Override
     public List<Rooms> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, Long propertyId) {
          List<Rooms> availableRooms = roomRepository.findAvailableRooms(checkInDate, checkOutDate, propertyId);
          availableRooms = availableRooms.stream()
                  .collect(Collectors.groupingBy(Rooms::getName,
                          Collectors.minBy(Comparator.comparing(Rooms::getPrice))))
                  .values()
                  .stream()
                  .filter(Optional::isPresent)
                  .map(Optional::get)
                  .sorted(Comparator.comparing(Rooms::getPrice))
                  .collect(Collectors.toList());
          PeakSeason peakSeason = peakSeasonService.getPeakSeasonByPropertyIdAndStartDate(propertyId, checkInDate);
          if (peakSeason != null) {
               Double markUpPercentage = peakSeason.getMarkUpPercentage() / 100;
               availableRooms.forEach(room -> {
                    Double originalPrice = room.getPrice();
                    Double newPrice = originalPrice + (originalPrice * markUpPercentage);
                    room.setPrice(newPrice);
               });
          }


          return availableRooms;
     }

     @Override
     public List<Rooms> getAllAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, Long propertyId) {
          Properties properties = propertiesService.getPropertiesById(propertyId);
          List<Rooms> availableRooms = roomRepository.findAvailableRooms(checkInDate, checkOutDate, properties.getId());
          PeakSeason peakSeason = peakSeasonService.getPeakSeasonByPropertyIdAndStartDate(properties.getId(), checkInDate);
          if (peakSeason != null) {
               Double markUpPercentage = peakSeason.getMarkUpPercentage() / 100;
               availableRooms.forEach(room -> {
                    Double originalPrice = room.getPrice();
                    Double newPrice = originalPrice + (originalPrice * markUpPercentage);
                    room.setPrice(newPrice);
               });
          }
          return availableRooms;
     }



    @Override
    public List<String> getRoomsTypeByPropertyId(Long propertyId) {
        List<Rooms> roomsList = roomRepository.getRoomsByPropertiesId(propertyId);
        Set<String> roomTypes = roomsList.stream().map(Rooms::getName).collect(Collectors.toSet());
        return new ArrayList<>(roomTypes);
    }

    @Override
    public Rooms saveRoom(Rooms rooms) {
        return roomRepository.save(rooms);
    }

     @Override
     public List<DailyRoomPrice> getLowestRoomPricesForMonth(int year, int month, Long propertyId) {
          List<DailyRoomPrice> dailyRoomPrices = new ArrayList<>();

          LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
          LocalDate lastDayOfMonth = firstDayOfMonth.with(TemporalAdjusters.lastDayOfMonth());

          for (LocalDate date = firstDayOfMonth; !date.isAfter(lastDayOfMonth); date = date.plusDays(1)) {
               List<Rooms> availableRooms = getAvailableRoomsWithoutMarkup(date, date.plusDays(1), propertyId);

               if (!availableRooms.isEmpty()) {
                    double lowestPrice = availableRooms.stream()
                            .mapToDouble(Rooms::getPrice)
                            .min()
                            .orElse(0.0);

                    PeakSeason peakSeason = peakSeasonService.getPeakSeasonByPropertyIdAndStartDate(propertyId, date);

                    if (peakSeason != null) {
                         Double markUpPercentage = peakSeason.getMarkUpPercentage() / 100;
                         lowestPrice = lowestPrice + (lowestPrice * markUpPercentage);
                    }

                    DailyRoomPrice dailyRoomPrice = new DailyRoomPrice().toDto(date, lowestPrice);
                    dailyRoomPrices.add(dailyRoomPrice);
               }
          }

          return dailyRoomPrices;
     }

     private List<Rooms> getAvailableRoomsWithoutMarkup(LocalDate checkInDate, LocalDate checkOutDate, Long propertyId) {
          return roomRepository.findAvailableRooms(checkInDate, checkOutDate, propertyId);
     }

      @Override
    public List<Rooms> getRoomsByNameAndPropertyId(String roomName, Long propertyId) {
        return roomRepository.getRoomsByNameAndPropertiesId(roomName, propertyId).orElseThrow(() -> new DataNotFoundException("Rooms not found" ));
    }
     @Override
     public Integer getTotalRooms(Long propertyId) {
          Properties properties = propertiesService.getPropertiesById(propertyId);
          return roomRepository.countByIsAvailableTrueAndDeletedAtIsNullAndProperties_Id(properties.getId());
     }

     @Override
     public Integer getOccupiedRooms(Long propertyId) {
          Properties properties = propertiesService.getPropertiesById(propertyId);
          LocalDate currentDate = LocalDate.now();
          return roomRepository.countCurrentlyOccupiedRooms(properties.getId(), currentDate);
     }

     public Rooms getRandomRoomByName(List<Rooms> availableRooms, String roomName) {
          List<Rooms> filteredRooms = availableRooms.stream()
                  .filter(room -> room.getName().equalsIgnoreCase(roomName))
                  .collect(Collectors.toList());

          if (filteredRooms.isEmpty()) {
               return null;
          }

          Random random = new Random();
          return filteredRooms.get(random.nextInt(filteredRooms.size()));
     }

     @Override
     public List<String> getMostBookedRoomNames(Long propertyId) {
          Properties properties = propertiesService.getPropertiesById(propertyId);
          List<String> roomName = roomRepository.findTop5RoomNamesByBookingCountAndPropertyId(properties.getId());
          roomName = roomName.stream().limit(5).collect(Collectors.toList());
          return roomName;
     }

     @Override
     public Rooms getRoomsBySlug(String slug) {
          return roomRepository.findBySlug(slug);
     }

     @Override
    public Float getRoomPrice(String slug, Long propertyId,  LocalDate checkInDate){
          Rooms rooms = getRoomsBySlug(slug);
          if(rooms == null){
               throw new DataNotFoundException("Room not found");
          }
          Double roomPrice = rooms.getPrice();
          PeakSeason peakSeason = peakSeasonService.getPeakSeasonByPropertyIdAndStartDate(propertyId, checkInDate);

          if (peakSeason != null) {
               Double markUpPercentage = peakSeason.getMarkUpPercentage() / 100;
               roomPrice = roomPrice + (roomPrice * markUpPercentage);
          }
          return roomPrice.floatValue();
     }
}


