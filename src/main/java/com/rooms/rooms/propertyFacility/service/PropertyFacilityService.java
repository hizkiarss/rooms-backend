package com.rooms.rooms.propertyFacility.service;

import com.rooms.rooms.propertyFacility.entity.PropertyFacility;

import java.util.List;

public interface PropertyFacilityService {
    List<PropertyFacility> findByPropertyId(Long propertyId);
    void deletePropertyFacility(List<Long> facilityIds, Long propertyId);
    List<PropertyFacility> addPropertyFacility(Long propertyId, Long[] facilityIds);

}
