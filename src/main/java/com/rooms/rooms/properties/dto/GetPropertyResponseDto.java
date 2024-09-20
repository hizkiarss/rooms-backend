package com.rooms.rooms.properties.dto;

import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.propertyFacility.entity.PropertyFacility;
import com.rooms.rooms.propertyPicture.entity.PropertyPicture;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

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
    private List<PropertyFacility> propertyFacility;
    private List<PropertyPicture> propertyPicture;

    public static GetPropertyResponseDto from(Properties property, String propertyCategories, List<PropertyFacility> propertyFacility, List<PropertyPicture> propertyPicture) {
        GetPropertyResponseDto dto = new GetPropertyResponseDto();
        dto.setId(property.getId());
        dto.setPropertyName(property.getName());
        dto.setPropertyCategories(propertyCategories);
        dto.setDescription(property.getDescription());
        dto.setCheckInTime(property.getCheckInTime());
        dto.setCheckOutTime(property.getCheckOutTime());
        dto.setAddress(property.getAddress());
        dto.setCity(property.getCity().getName());
        dto.setPropertyFacility(propertyFacility);
        dto.setPropertyPicture(propertyPicture);
        return dto;
    }
}
