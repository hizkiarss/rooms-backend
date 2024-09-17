package com.rooms.rooms.properties.dto;

import com.rooms.rooms.city.entity.City;
import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.propertyCategories.entity.PropertyCategories;
import lombok.Data;

import java.time.LocalTime;

@Data
public class UpdatePropertyRequestDto {
    private String propertyName;
    private String propertyCategories;
    private String description;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private String address;
    private String city;

    public void toEntity(Properties property, City city, PropertyCategories category) {
        property.setName(this.propertyName);
        property.setDescription(this.description);
        property.setAddress(this.address);
        property.setCheckInTime(this.checkInTime);
        property.setCheckOutTime(this.checkOutTime);
        property.setCity(city);
        property.setPropertyCategories(category);
    }
}
