package com.rooms.rooms.peakSeason.service.impl;

import com.rooms.rooms.peakSeason.dto.ChangePriceForPeakSeasonDto;
import com.rooms.rooms.peakSeason.entity.PeakSeason;
import com.rooms.rooms.peakSeason.repository.PeakSeasonRepository;
import com.rooms.rooms.peakSeason.service.PeakSeasonService;
import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.properties.service.PropertiesService;
import com.rooms.rooms.rooms.entity.Rooms;
import com.rooms.rooms.rooms.service.RoomsService;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
public class PeakSeasonServiceImpl implements PeakSeasonService {
    private final PeakSeasonRepository peakSeasonRepository;
    private final PropertiesService propertiesService;
    private final RoomsService roomsService;

    public PeakSeasonServiceImpl(PeakSeasonRepository peakSeasonRepository, PropertiesService propertiesService, RoomsService roomsService) {
        this.peakSeasonRepository = peakSeasonRepository;
        this.propertiesService = propertiesService;
        this.roomsService = roomsService;
    }

    @Override
    public PeakSeason changePriceForPeakSeason(ChangePriceForPeakSeasonDto dto) {
        Properties properties = propertiesService.getPropertiesById(dto.getPropertyId());
        Rooms rooms = roomsService.getRoomsById(dto.getRoomId());
        return peakSeasonRepository.save(dto.toEntity(properties, rooms)) ;
    }

    @Override
    public List<PeakSeason> getPeakSeasonByPropertyId(Long propertyId) {
        return peakSeasonRepository.findByPropertiesId(propertyId);
    }

    @Override
    public List<PeakSeason> findMarkedUpRooms(List<Long> roomId, LocalDate checkInDate) {
        return peakSeasonRepository.findMarkedUpRooms(roomId, checkInDate);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void resetPriceAfterPeakSeason() {
        LocalDate now = LocalDate.now();
        List<PeakSeason> endedPeakSeasons = peakSeasonRepository.findEndedPeakSeasons(now);

        for (PeakSeason peakSeason : endedPeakSeasons) {
            Rooms room = peakSeason.getRoom();
            double currentPrice = room.getPrice();
            double markupAmount = currentPrice * (peakSeason.getMarkUpPercentage() / 100);
            double basePrice = currentPrice - markupAmount;
            room.setPrice(basePrice);
            roomsService.saveRoom(room);
            peakSeason.setDeletedAt(Instant.now());
            peakSeasonRepository.save(peakSeason);
        }
    }
}

