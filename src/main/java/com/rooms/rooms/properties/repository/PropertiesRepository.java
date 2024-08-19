package com.rooms.rooms.properties.repository;

import com.rooms.rooms.properties.entity.Properties;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertiesRepository extends JpaRepository<Properties, Long> {
}
