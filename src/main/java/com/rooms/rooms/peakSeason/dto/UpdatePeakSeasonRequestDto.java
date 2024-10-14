package com.rooms.rooms.peakSeason.dto;

import com.rooms.rooms.peakSeason.entity.PeakSeason;
import lombok.Data;

import java.time.LocalDate;
@Data
public class UpdatePeakSeasonRequestDto{
    private String name;
    private Long peakSeasonId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double markUpPercentage;

    public PeakSeason updatePeakSeasonToEntity(PeakSeason currentPeakSeason){
        if (this.startDate != null) {
            currentPeakSeason.setStartDate(this.startDate);
        }

        if (this.name != null) {
            currentPeakSeason.setName(this.name);
        }

        if (this.endDate != null) {
            currentPeakSeason.setEndDate(this.endDate);
        }

        if (this.markUpPercentage != null) {
            currentPeakSeason.setMarkUpPercentage(this.markUpPercentage);
        }
        return currentPeakSeason;
    }
}