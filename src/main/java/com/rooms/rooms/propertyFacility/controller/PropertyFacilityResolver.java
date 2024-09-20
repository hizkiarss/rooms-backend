package com.rooms.rooms.propertyFacility.controller;

import com.rooms.rooms.propertyFacility.entity.PropertyFacility;
import com.rooms.rooms.propertyFacility.repository.PropertyFacilityRepository;
import com.rooms.rooms.propertyFacility.service.PropertyFacilityService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class PropertyFacilityResolver {
    private final PropertyFacilityService propertyFacilityService;

    public PropertyFacilityResolver(PropertyFacilityRepository propertyFacilityRepository, PropertyFacilityService propertyFacilityService) {
        this.propertyFacilityService = propertyFacilityService;
    }

    @QueryMapping(value = "getPropertyFacilities")
    public List<PropertyFacility> getPropertyFacilities(@Argument Long id) {
        return propertyFacilityService.findByPropertyId(id);
    }

    @MutationMapping(value = "addPropertyFacilities")
    public List<PropertyFacility> addPropertyFacility(@Argument Long id, @Argument List<String> facilitiesId) {
        Long[] facilitiesIdLong = facilitiesId.stream()
                .map(Long::parseLong)
                .toArray(Long[]::new);
        return propertyFacilityService.addPropertyFacility(id, facilitiesIdLong);
    }

    @MutationMapping(value = "deletePropertyFacilities")
    public String deletePropertyFacility(@Argument Long id, @Argument List<String> facilitiesId) {
        List<Long> facilitiesIdLong = facilitiesId.stream()
                .map(Long::parseLong)
                .toList();
        propertyFacilityService.deletePropertyFacility(facilitiesIdLong, id);
        return "Facilities deleted";
    }
}
