package com.rooms.rooms.city.controller;

import com.rooms.rooms.city.entity.City;
import com.rooms.rooms.city.service.impl.CityService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CityResolver {
    private final CityService cityService;

    public CityResolver(CityService cityService) {
        this.cityService = cityService;
    }

    @QueryMapping("findCityByName")
    public List<City> findCityByName(@Argument String name) {
        return cityService.findByName(name);
    }

    @QueryMapping("findAllCity")
    public List<City> findAllCity() {
        return cityService.findAll();
    }
}
