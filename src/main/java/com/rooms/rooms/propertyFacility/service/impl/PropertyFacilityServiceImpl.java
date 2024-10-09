package com.rooms.rooms.propertyFacility.service.impl;

import com.rooms.rooms.exceptions.DataNotFoundException;
import com.rooms.rooms.facilities.entity.Facilities;
import com.rooms.rooms.facilities.service.FacilitiesService;
import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.properties.service.PropertiesService;
import com.rooms.rooms.propertyFacility.entity.PropertyFacility;
import com.rooms.rooms.propertyFacility.repository.PropertyFacilityRepository;
import com.rooms.rooms.propertyFacility.service.PropertyFacilityService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PropertyFacilityServiceImpl implements PropertyFacilityService {
    private final PropertyFacilityRepository propertyFacilityRepository;
    private final PropertiesService propertiesService;
    private final FacilitiesService facilitiesService;

    public PropertyFacilityServiceImpl(PropertyFacilityRepository propertyFacilityRepository, PropertiesService propertiesService, FacilitiesService facilitiesService) {
        this.propertyFacilityRepository = propertyFacilityRepository;
        this.propertiesService = propertiesService;
        this.facilitiesService = facilitiesService;
    }

    @Override
    public List<PropertyFacility> findByPropertyId(Long propertyId) {
        propertiesService.getPropertiesById(propertyId);
        return propertyFacilityRepository.findByPropertyId(propertyId);
    }

    @Override
    public List<PropertyFacility> addPropertyFacility(Long propertyId, Long[] facilityIds) {
        Properties currentProperty = propertiesService.getPropertiesById(propertyId);

        List<Facilities> listedFacilities = facilitiesService.findAllById(facilityIds);
        System.out.println(listedFacilities);
        List<PropertyFacility> currentPropertyFacilities = propertyFacilityRepository.findByPropertyId(propertyId);

        Set<Long> currentPropertyFacilityIds = currentPropertyFacilities.stream()
                .map(propertyFacility -> Long.valueOf(propertyFacility.getId()))
                .collect(Collectors.toSet());

        Set<Long> currentFacilityIds = currentPropertyFacilities.stream()
                .map(propertyFacility -> propertyFacility.getFacilities().getId())
                .collect(Collectors.toSet());

        Set<Long> newFacilityIds = listedFacilities.stream()
                .map(Facilities::getId)
                .filter(id -> !currentPropertyFacilityIds.contains(id)) // Check if facility ID is already linked
                .collect(Collectors.toSet());

        List<Long> notFoundFacilities = Arrays.stream(facilityIds)
                .filter(id -> !newFacilityIds.contains(id))
                .toList();


        if (!notFoundFacilities.isEmpty()) {
            throw new DataNotFoundException("Facilities not found with these ids: " + notFoundFacilities);
        }

        List<PropertyFacility> propertyFacilities = listedFacilities.stream()
                .filter(facility -> newFacilityIds.contains(facility.getId()))
                .filter(facility -> !currentFacilityIds.contains(facility.getId()))
                .map(facility -> {
                    PropertyFacility propertyFacility = new PropertyFacility();
                    propertyFacility.setProperty(currentProperty);
                    propertyFacility.setFacilities(facility);
                    return propertyFacility;
                }).toList();
        return propertyFacilityRepository.saveAll(propertyFacilities);
    }

    @Override
    public void deletePropertyFacility(List<Long> facilityIds, Long propertyId) {
        propertiesService.getPropertiesById(propertyId);
        Set<Long> facilityIdSet = new HashSet<>(facilityIds);

        List<PropertyFacility> currentPropertyFacilities = findByPropertyId(propertyId);
        currentPropertyFacilities.forEach(propertyFacility -> {
            Long facilityId = propertyFacility.getFacilities().getId();

            if (facilityIdSet.contains(facilityId)) {
                propertyFacility.setDeletedAt(Instant.now());
                propertyFacilityRepository.save(propertyFacility);
            }
        });
    }

}

