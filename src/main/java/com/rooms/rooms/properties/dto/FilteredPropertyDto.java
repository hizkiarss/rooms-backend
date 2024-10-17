package com.rooms.rooms.properties.dto;

import com.rooms.rooms.properties.entity.Properties;
import lombok.Data;

@Data
public class FilteredPropertyDto {
    private Properties property;
    private Double price;
    private Boolean isBreakfast;

    public FilteredPropertyDto(PropertyProjection projection) {
        this.property = projection.getProperty();
        this.price = projection.getPrice();
        this.isBreakfast = projection.getIsBreakfast();
    }
}
