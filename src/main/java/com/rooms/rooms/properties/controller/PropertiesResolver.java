package com.rooms.rooms.properties.controller;

import com.rooms.rooms.properties.dto.*;
import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.properties.service.PropertiesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class PropertiesResolver {
    private PropertiesService propertiesService;

    public PropertiesResolver(PropertiesService propertiesService) {
        this.propertiesService = propertiesService;
    }

    @QueryMapping(value = "getPropertiesById")
    public Properties getPropertyById(@Argument Long id) {
        return propertiesService.getPropertiesById(id);
    }

    @QueryMapping(value = "getAllProperties")
    public List<Properties> getAllProperties() {
        return propertiesService.getAllProperties();
    }

    @QueryMapping(value = "getFilteredProperties")
    public PagedPropertyResult getFilteredProperties(
            @Argument Double rating,
            @Argument Double startPrice,
            @Argument Double endPrice,
            @Argument Boolean isBreakfast,
            @Argument String city,
            @Argument int page,
            @Argument String category) {

        int pageNumber = page - 1;
        Pageable pageable = PageRequest.of(pageNumber, 10);
        return propertiesService.getAllPropertyProjections(rating, startPrice, endPrice, isBreakfast, city, pageable, category);
    }

    @MutationMapping(value = "createProperties")
    public String createProperties(@Argument("input") CreatePropertyRequestDto dto) {
        propertiesService.createProperties(dto);
        return "success";
    }

    @MutationMapping(value = "updateProperties")
    public String updateProperties(@Argument Long id, @Argument("input") UpdatePropertyRequestDto dto) {
        propertiesService.updateProperties(id, dto);
        return "success";
    }

    @MutationMapping(value = "deleteProperties")
    public String deleteProperties(@Argument Long id) {
        propertiesService.deleteProperties(id);
        return "Your property has been deleted";
    }

}
