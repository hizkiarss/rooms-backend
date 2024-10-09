package com.rooms.rooms.facilities.service;

import com.rooms.rooms.facilities.entity.Facilities;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FacilitiesService {
    Optional<Facilities> findById(Long id);
    List<Facilities> findAllById(Long[] ids);
}
