package com.rooms.rooms.peakSeason.dto;

import com.rooms.rooms.peakSeason.entity.PeakSeason;
import com.rooms.rooms.properties.entity.Properties;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ChangePriceForPeakSeasonDto {
    private String name;
    private Long propertyId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double markupValue;
    private String markupType;

    public PeakSeason toEntity (Properties properties){
        PeakSeason peakSeason = new PeakSeason();
        peakSeason.setName(name);
        peakSeason.setProperties(properties);
        peakSeason.setStartDate(this.startDate);
        peakSeason.setEndDate(this.endDate);
        peakSeason.setMarkUpValue(this.markupValue);
        peakSeason.setMarkupType(this.markupType);
        return peakSeason;
    }
}
