package com.rooms.rooms.peakSeason.service;

import com.rooms.rooms.peakSeason.dto.ChangePriceForPeakSeasonDto;
import com.rooms.rooms.peakSeason.dto.UpdatePeakSeasonRequestDto;
import com.rooms.rooms.peakSeason.entity.PeakSeason;

import java.time.LocalDate;
import java.util.List;

public interface PeakSeasonService {
    List<PeakSeason> getPeakSeasonByPropertyId(Long propertyId);
    PeakSeason changePriceForPeakSeason(ChangePriceForPeakSeasonDto dto);
    List<PeakSeason> findMarkedUpRooms(Long roomId, LocalDate checkInDate);
    PeakSeason getPeakSeasonByPropertyIdAndStartDate(Long propertyId, LocalDate startDate);
    PeakSeason updatePeakSeason(UpdatePeakSeasonRequestDto dto);
    void deletePeakSeason(Long peakSeasonId);
}
