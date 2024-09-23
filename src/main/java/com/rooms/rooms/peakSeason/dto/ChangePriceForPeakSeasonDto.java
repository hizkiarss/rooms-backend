package com.rooms.rooms.peakSeason.dto;

import com.rooms.rooms.peakSeason.entity.PeakSeason;
import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.rooms.entity.Rooms;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ChangePriceForPeakSeasonDto {
    private Long propertyId;
    private Long roomId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double markUpPercentage;

    public PeakSeason toEntity (Properties properties, Rooms rooms){
        PeakSeason peakSeason = new PeakSeason();
        peakSeason.setProperties(properties);
        peakSeason.setRoom(rooms);
        peakSeason.setStartDate(this.startDate);
        peakSeason.setEndDate(this.endDate);
        peakSeason.setMarkUpPercentage(this.markUpPercentage);
        return peakSeason;
    }
}
