package com.rooms.rooms.city.service;

import com.rooms.rooms.city.entity.City;
import com.rooms.rooms.city.repository.CityRepository;
import com.rooms.rooms.city.service.impl.CityService;
import com.rooms.rooms.exceptions.DataNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImplementation implements CityService {
    private final CityRepository cityRepository;

    public CityServiceImplementation(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public List<City> findAll() {
        return cityRepository.findAll();
    }

    @Override
    public List<City> findByName(String name) {
        return cityRepository.findCityListByName(name);
    }
    @Override
    public City findACity(String name) {
        return cityRepository.findByName(name).orElseThrow(()->new DataNotFoundException(name));
    }
}
