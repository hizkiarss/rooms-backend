package com.rooms.rooms.properties.repository;

import com.rooms.rooms.properties.entity.Properties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PropertiesRepository extends JpaRepository<Properties, Long> {
    Optional<Properties> findByName(String name);

    @Query("SELECT p FROM Properties p JOIN PropertyPicture pp ON pp.properties.id = p.id WHERE pp.id IN :pictureIds")
    Properties findPropertiesByPropertyPictureIds(@Param("pictureIds") Long pictureId);

    @Query("SELECT p FROM Properties p JOIN FETCH p.users WHERE p.id = :id")
    Optional<Properties> findByIdWithUsers(@Param("id") Long id);


}