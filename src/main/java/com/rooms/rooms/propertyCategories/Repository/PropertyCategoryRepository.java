package com.rooms.rooms.propertyCategories.Repository;

import com.rooms.rooms.propertyCategories.entity.PropertyCategories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PropertyCategoryRepository extends JpaRepository<PropertyCategories, Long> {
    Optional<PropertyCategories> findByName(String name);
}
