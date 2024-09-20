package com.rooms.rooms.properties.service.impl;

import com.rooms.rooms.city.entity.City;
import com.rooms.rooms.city.service.impl.CityService;
import com.rooms.rooms.exceptions.DataNotFoundException;
import com.rooms.rooms.properties.dto.CreatePropertyRequestDto;
import com.rooms.rooms.properties.dto.GetPropertyResponseDto;
import com.rooms.rooms.properties.dto.PropertyOwnerDto;
import com.rooms.rooms.properties.dto.UpdatePropertyRequestDto;
import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.properties.repository.PropertiesRepository;
import com.rooms.rooms.properties.service.PropertiesService;
import com.rooms.rooms.propertyCategories.Service.PropertyCategoriesService;
import com.rooms.rooms.propertyCategories.entity.PropertyCategories;
import com.rooms.rooms.propertyFacility.entity.PropertyFacility;
import com.rooms.rooms.propertyFacility.service.PropertyFacilityService;
import com.rooms.rooms.propertyPicture.entity.PropertyPicture;
import com.rooms.rooms.propertyPicture.service.PropertyPictureService;
import com.rooms.rooms.users.entity.Users;
import com.rooms.rooms.users.repository.UsersRepository;
import com.rooms.rooms.users.service.UsersService;
import com.rooms.rooms.users.service.impl.UsersServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class PropertiesServiceImpl implements PropertiesService {
    private final PropertiesRepository propertiesRepository;
    private final UsersService usersService;
    private final PropertyCategoriesService propertyCategoriesService;
    private final CityService cityService;


    public PropertiesServiceImpl(PropertiesRepository propertiesRepository, UsersService usersService, PropertyCategoriesService propertyCategoriesService, CityService cityService) {
        this.propertiesRepository = propertiesRepository;
        this.usersService = usersService;
        this.propertyCategoriesService = propertyCategoriesService;
        this.cityService = cityService;

    }

    @Override
    public List<Properties> getAllProperties() {
        return propertiesRepository.findAll();
    }

    @Override
//    @Cacheable(value = "getPropertiesById", key = "#id")
    public Properties getPropertiesById(Long id) {
        return propertiesRepository.findById(id).orElseThrow(() -> new DataNotFoundException("No properties found with id: " + id));
    }


    @Transactional
    @Override
    public Properties createProperties(CreatePropertyRequestDto dto) {
        Users user = usersService.findByEmail(dto.getEmail());
        PropertyCategories category = propertyCategoriesService.getPropertyCategoriesByName(dto.getPropertyCategories());
        City city = cityService.findACity(dto.getCity());
        Properties newProperties = new Properties();
        newProperties.setUsers(user);
        newProperties.setPropertyCategories(category);
        newProperties.setCity(city);
        newProperties.setAddress(dto.getAddress());
        newProperties.setDescription(dto.getDescription());
        newProperties.setCheckInTime(dto.getCheckInTime());
        newProperties.setCheckOutTime(dto.getCheckOutTime());
        newProperties.setName(dto.getPropertyName());
        return propertiesRepository.save(newProperties);
    }

    @Transactional
    @Override
    public Properties updateProperties(Long propertyId, UpdatePropertyRequestDto dto) {
        Properties currentProperty = propertiesRepository.findById(propertyId).orElseThrow(() -> new DataNotFoundException("Properties with id " + propertyId + " not found"));
        City city = cityService.findACity(dto.getCity());
        PropertyCategories category = propertyCategoriesService.getPropertyCategoriesByName(dto.getPropertyCategories());
        dto.toEntity(currentProperty, city, category);
        propertiesRepository.save(currentProperty);
        return currentProperty;
    }

    @Transactional
    @Override
    public void deleteProperties(Long id) {
        Properties currentProperty = propertiesRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Properties with id " + id + " not found"));
        currentProperty.setDeletedAt(Instant.now());
        propertiesRepository.save(currentProperty);
    }

    @Override
    public Properties findPropertiesByPropertyPicture(Long propertiesId) {
        return propertiesRepository.findPropertiesByPropertyPictureIds(propertiesId);
    }

    @Override
    @Transactional(readOnly = true)
    public PropertyOwnerDto getPropertyOwnerById(Long id) {
        Properties property = propertiesRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Property not found"));
        PropertyOwnerDto dto = new PropertyOwnerDto();
        dto.setId(property.getId());
        dto.setEmail(property.getUsers().getEmail());
        return dto;
    }
}
