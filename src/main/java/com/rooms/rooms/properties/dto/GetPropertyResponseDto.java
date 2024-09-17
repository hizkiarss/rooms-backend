package com.rooms.rooms.properties.dto;

import com.rooms.rooms.properties.entity.Properties;
import lombok.Data;

import java.time.LocalTime;

@Data
public class GetPropertyResponseDto {
    private Long id;
    private String propertyName;
    private String propertyCategories;
    private String description;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private String address;
    private String city;

    public static GetPropertyResponseDto from(Properties property, String city, String propertyCategories) {
        GetPropertyResponseDto dto = new GetPropertyResponseDto();
        dto.setId(property.getId());
        dto.setPropertyName(property.getName());
        dto.setPropertyCategories(propertyCategories);
        dto.setDescription(property.getDescription());
        dto.setCheckInTime(property.getCheckInTime());
        dto.setCheckOutTime(property.getCheckOutTime());
        dto.setAddress(property.getAddress());
        dto.setCity(city);

        return dto;
    }
}
