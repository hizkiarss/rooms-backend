package com.rooms.rooms.rooms.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DailyRoomPrice {
    private LocalDate date;
    private double price;

    public DailyRoomPrice toDto(LocalDate date, double price){
        DailyRoomPrice dto = new DailyRoomPrice();
        dto.setDate(date);
        dto.setPrice(price);
        return dto;
    }
}
