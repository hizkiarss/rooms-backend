package com.rooms.rooms.bedTypes.controller;

import com.rooms.rooms.bedTypes.entity.BedTypes;
import com.rooms.rooms.bedTypes.service.BedTypesService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class BedTypeResolver {
    private final BedTypesService bedTypesService;

    public BedTypeResolver(BedTypesService bedTypesService) {
        this.bedTypesService = bedTypesService;
    }


    @QueryMapping(value = "getBedTypesByName")
    public BedTypes getBedTypesByName(@Argument String name) {
       return bedTypesService.getBedTypesByName(name);
    }
}
