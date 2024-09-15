package com.rooms.rooms.properties.service;

import com.rooms.rooms.properties.dto.CreatePropertyRequestDto;
import com.rooms.rooms.properties.dto.UpdatePropertyRequestDto;
import com.rooms.rooms.properties.entity.Properties;

public interface PropertiesService {
     Properties getPropertiesById(Long id);
     Properties getPropertiesByName(String name);
     Properties createProperties(CreatePropertyRequestDto dto);
     Properties updateProperties(Long id, UpdatePropertyRequestDto dto);
     void deleteProperties(Long id);
}
