package com.rooms.rooms.properties.dto;

import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalTime;

@Data
public class CreatePropertyRequestDto {
    private String email;
    private String propertyName;
    private String propertyCategories;
    private String description;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private String address;
    private String city;

}
