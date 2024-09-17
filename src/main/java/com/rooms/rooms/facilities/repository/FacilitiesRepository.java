package com.rooms.rooms.facilities.repository;

import com.rooms.rooms.facilities.entity.Facilities;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacilitiesRepository extends JpaRepository<Facilities, Long> {
}
