package com.rooms.rooms.peakSeason.dto;

import com.rooms.rooms.peakSeason.entity.PeakSeason;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdatePeakSeasonRequestDto {
    private String name;
    private Long peakSeasonId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double markUpValue;
    private String markupType;


    public PeakSeason updatePeakSeasonToEntity(PeakSeason currentPeakSeason) {
        if (this.startDate != null) {
            currentPeakSeason.setStartDate(this.startDate);
        }

        if (this.name != null) {
            currentPeakSeason.setName(this.name);
        }

        if (this.endDate != null) {
            currentPeakSeason.setEndDate(this.endDate);
        }

        if (this.markUpValue != null) {
            currentPeakSeason.setMarkUpValue(this.markUpValue);
        }

        if (this.markupType != null) {
            currentPeakSeason.setMarkUpType(this.markupType);
        }


        return currentPeakSeason;
    }
}