package com.rooms.rooms.peakSeason.service.impl;

import com.rooms.rooms.exceptions.DataNotFoundException;
import com.rooms.rooms.peakSeason.dto.ChangePriceForPeakSeasonDto;
import com.rooms.rooms.peakSeason.dto.UpdatePeakSeasonRequestDto;
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
import java.util.Optional;

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
        return peakSeasonRepository.save(dto.toEntity(properties));
    }

    @Override
    public List<PeakSeason> getPeakSeasonByPropertyId(Long propertyId) {
        return peakSeasonRepository.findByPropertiesId(propertyId);
    }

    @Override
    public List<PeakSeason> findMarkedUpRooms(Long propertyId, LocalDate checkInDate) {
        return peakSeasonRepository.findMarkedUpRooms(propertyId, checkInDate);
    }

    @Override
    public PeakSeason getPeakSeasonByPropertyIdAndStartDate(Long propertyId, LocalDate startDate) {
        Optional<PeakSeason> peakSeason = peakSeasonRepository.findPeakSeasonByPropertyIdAndStartDate(propertyId, startDate);
        if (peakSeason.isEmpty()) {
            return null;
        }
        return peakSeason.get();
    }

    @Override
    public void deletePeakSeason(Long peakSeasonId) {
        PeakSeason chosenPeakSeason = peakSeasonRepository.findById(peakSeasonId).orElseThrow(() -> new DataNotFoundException("Peak Season not found"));
        chosenPeakSeason.setDeletedAt(Instant.now());
        peakSeasonRepository.save(chosenPeakSeason);
    }

    @Override
    public PeakSeason updatePeakSeason(UpdatePeakSeasonRequestDto dto) {
        PeakSeason currentPeakSeason = peakSeasonRepository.findById(dto.getPeakSeasonId()).orElseThrow(() -> new DataNotFoundException("Peak Season not found"));
        PeakSeason updatedPeakSeason = dto.updatePeakSeasonToEntity(currentPeakSeason);
        return peakSeasonRepository.save(updatedPeakSeason);
    }

}

