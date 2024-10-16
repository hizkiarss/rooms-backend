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
import org.springframework.security.access.prepost.PreAuthorize;
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


    @QueryMapping("getPropertiesBySlug")
    public Properties getPropertiesBySlug(@Argument String slug) {
        return propertiesService.getPropertiesBySlug(slug);
    }

    @QueryMapping("getPropertiesByOwnerEmail")
    public List<Properties> getPropertiesByOwnerEmail(@Argument String email) {
        System.out.println(email +  "kocakkkkkkkk");
        return propertiesService.getPropertiesByOwnerEmail(email);
    }


    @QueryMapping(value = "getFilteredProperties")
    public PagedPropertyResult getFilteredProperties(
            @Argument Double rating,
            @Argument Double startPrice,
            @Argument Double endPrice,
            @Argument Boolean isBreakfast,
            @Argument String city,
            @Argument int page,
            @Argument String category,
            @Argument String sortBy) {

        return propertiesService.getAllPropertyProjections(rating, startPrice, endPrice, isBreakfast, city, page, category, sortBy);
    }

    @MutationMapping(value = "addSlug")
    public String addSlug() {
        propertiesService.addSlug();
        return "slugs added";
    }

    @PreAuthorize("hasAuthority('SCOPE_TENANT')")
    @MutationMapping(value = "createProperties")
    public Properties createProperties(@Argument("input") CreatePropertyRequestDto dto) {
        return propertiesService.createProperties(dto);
    }

    @PreAuthorize("hasAuthority('SCOPE_TENANT')")
    @MutationMapping(value = "updateProperties")
    public String updateProperties(@Argument Long id, @Argument("input") UpdatePropertyRequestDto dto) {
        propertiesService.updateProperties(id, dto);
        return "success";
    }

    @PreAuthorize("hasAuthority('SCOPE_TENANT')")
    @MutationMapping(value = "deleteProperties")
    public String deleteProperties(@Argument Long id) {
        propertiesService.deleteProperties(id);
        return "Your property has been deleted";
    }

}
