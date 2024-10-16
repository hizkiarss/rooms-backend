package com.rooms.rooms.properties.service;

import com.rooms.rooms.properties.dto.*;
import com.rooms.rooms.properties.entity.Properties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface PropertiesService {
    Properties getPropertiesById(Long id);

//    GetPropertyResponseDto getPropertiesByName(String name);

    Properties createProperties(CreatePropertyRequestDto dto);

    Properties updateProperties(Long id, UpdatePropertyRequestDto dto);

    void addSlug();

    Properties getPropertiesBySlug(String slug);

    void deleteProperties(Long id);

    List<Properties> getAllProperties();

    Properties findPropertiesByPropertyPicture(Long propertiesId);

    PropertyOwnerDto getPropertyOwnerById(Long id);

    List<Properties> getPropertiesByOwnerEmail(String ownerEmail);

    PagedPropertyResult getAllPropertyProjections(Double rating, Double startPrice, Double endPrice, Boolean isBreakfast,
                                                  String city, Integer page, String category, String sortBy,
                                                  LocalDate checkinDate, LocalDate checkoutDate);
}
