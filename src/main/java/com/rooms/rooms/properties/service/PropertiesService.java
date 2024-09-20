package com.rooms.rooms.properties.service;

import com.rooms.rooms.properties.dto.CreatePropertyRequestDto;
import com.rooms.rooms.properties.dto.GetPropertyResponseDto;
import com.rooms.rooms.properties.dto.PropertyOwnerDto;
import com.rooms.rooms.properties.dto.UpdatePropertyRequestDto;
import com.rooms.rooms.properties.entity.Properties;

import java.util.List;

public interface PropertiesService {
    Properties getPropertiesById(Long id);

//    GetPropertyResponseDto getPropertiesByName(String name);

    Properties createProperties(CreatePropertyRequestDto dto);

    Properties updateProperties(Long id, UpdatePropertyRequestDto dto);


    void deleteProperties(Long id);

    List<Properties> getAllProperties();

    Properties findPropertiesByPropertyPicture(Long propertiesId);

    PropertyOwnerDto getPropertyOwnerById(Long id);
}
