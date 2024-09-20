package com.rooms.rooms.bedTypes.service.impl;

import com.rooms.rooms.bedTypes.entity.BedTypes;
import com.rooms.rooms.bedTypes.repository.BedTypeRepository;
import com.rooms.rooms.bedTypes.service.BedTypesService;
import org.springframework.stereotype.Service;

@Service
public class BedTypesServiceImpl implements BedTypesService {
    private final BedTypeRepository bedTypeRepository;

    public BedTypesServiceImpl(BedTypeRepository bedTypeRepository) {
        this.bedTypeRepository = bedTypeRepository;
    }

    @Override
    public BedTypes getBedTypesByName(String bedTypeName) {
        return bedTypeRepository.findByName(bedTypeName);
    }
}
