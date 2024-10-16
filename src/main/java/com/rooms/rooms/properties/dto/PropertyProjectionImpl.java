package com.rooms.rooms.properties.dto;

import com.rooms.rooms.properties.entity.Properties;

public class PropertyProjectionImpl implements PropertyProjection {
    private Properties property;
    private Double basePrice;
    private Double markupPercentage;
    private Boolean isBreakfast;

    public PropertyProjectionImpl(Properties property, Double basePrice, Double markupPercentage, Boolean isBreakfast) {
        this.property = property;
        this.basePrice = basePrice;
        this.markupPercentage = markupPercentage;
        this.isBreakfast = isBreakfast;
    }

    @Override
    public Properties getProperty() {
        return property;
    }

    @Override
    public Double getPrice() {
        return basePrice * (1 + markupPercentage / 100);
    }

    @Override
    public Boolean getIsBreakfast() {
        return isBreakfast;
    }
}
