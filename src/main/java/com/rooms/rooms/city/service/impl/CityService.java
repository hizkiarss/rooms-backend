package com.rooms.rooms.city.service.impl;

import com.rooms.rooms.city.entity.City;

import java.util.List;

public interface CityService {
    List<City> findAll();
    List<City> findByName(String name);

}
