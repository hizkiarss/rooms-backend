package com.rooms.rooms.bedTypes.service;

import com.rooms.rooms.bedTypes.entity.BedTypes;

public interface BedTypesService {
    BedTypes getBedTypesByName(String bedTypeName);
}
