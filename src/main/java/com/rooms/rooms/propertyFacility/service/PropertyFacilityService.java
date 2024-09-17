package com.rooms.rooms.propertyFacility.service;

import com.rooms.rooms.propertyFacility.entity.PropertyFacility;

import java.util.List;

public interface PropertyFacilityService {
    List<PropertyFacility> findByPropertyId(Long propertyId);

    List<PropertyFacility> createPropertyFacility(Long propertyId, Long[] facilityIds);
}
