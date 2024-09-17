package com.rooms.rooms.propertyFacility.repository;

import com.rooms.rooms.propertyFacility.entity.PropertyFacility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyFacilityRepository extends JpaRepository<PropertyFacility, Long> {
    List<PropertyFacility> findByPropertyId(Long propertyId);
}
