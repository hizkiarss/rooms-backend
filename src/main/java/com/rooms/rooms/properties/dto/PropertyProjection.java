package com.rooms.rooms.properties.dto;

import com.rooms.rooms.properties.entity.Properties;

public interface PropertyProjection {
    Properties getProperty();
    Double getPrice();
    Boolean getIsBreakfast();
}