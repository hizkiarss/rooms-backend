package com.rooms.rooms.bedTypes.repository;

import com.rooms.rooms.bedTypes.entity.BedTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BedTypeRepository extends JpaRepository<BedTypes, Long> {
    BedTypes findByName(String name);
}
