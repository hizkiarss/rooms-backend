package com.rooms.rooms.facilities.service.impl;

import com.rooms.rooms.facilities.entity.Facilities;
import com.rooms.rooms.facilities.repository.FacilitiesRepository;
import com.rooms.rooms.facilities.service.FacilitiesService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class FacilitiesServiceImpl implements FacilitiesService {
    private final FacilitiesRepository facilitiesRepository;

    public FacilitiesServiceImpl(FacilitiesRepository facilitiesRepository) {
        this.facilitiesRepository = facilitiesRepository;
    }

    @Override
    public Optional<Facilities> findById(Long id) {
        return facilitiesRepository.findById(id);
    }

    @Override
    public List<Facilities> findAllById(Long[] ids) {
        return facilitiesRepository.findAllById(List.of(ids));
    }
}
