package com.rooms.rooms.properties.repository;

import com.rooms.rooms.properties.entity.Properties;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PropertiesRepository extends JpaRepository<Properties, Long> {
    Optional<Properties> findByName(String name);
}
