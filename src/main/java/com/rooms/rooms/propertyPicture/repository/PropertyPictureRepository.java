package com.rooms.rooms.propertyPicture.repository;

import com.rooms.rooms.propertyPicture.entity.PropertyPicture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Properties;

public interface PropertyPictureRepository extends JpaRepository<PropertyPicture, Long> {
    List<PropertyPicture> findPropertyPicturesByPropertiesId(Long id);
}
