package com.rooms.rooms.city.repository;

import com.rooms.rooms.city.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long> {
    @Query("SELECT c FROM City c WHERE c.name LIKE :name%")
    List<City> findByName(@Param("name") String name);
}
