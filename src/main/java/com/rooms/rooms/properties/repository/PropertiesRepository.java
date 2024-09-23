package com.rooms.rooms.properties.repository;

import com.rooms.rooms.properties.dto.PropertyProjection;
import com.rooms.rooms.properties.entity.Properties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PropertiesRepository extends JpaRepository<Properties, Long> {
    Optional<Properties> findByName(String name);

    @Query("SELECT p FROM Properties p JOIN PropertyPicture pp ON pp.properties.id = p.id WHERE pp.id IN :pictureIds")
    Properties findPropertiesByPropertyPictureIds(@Param("pictureIds") Long pictureId);

    @Query("SELECT p FROM Properties p JOIN FETCH p.users WHERE p.id = :id")
    Optional<Properties> findByIdWithUsers(@Param("id") Long id);

    @Query("SELECT DISTINCT p as property, r.price as price, r.includeBreakfast as isBreakfast " +
            "FROM Properties p " +
            "LEFT JOIN p.rooms r " +
            "LEFT JOIN p.propertyCategories pc " +
            "WHERE p.deletedAt IS NULL " +
            "AND r.deletedAt IS NULL " +
            "AND p.city.deletedAt IS NULL " +
            "AND (:rating IS NULL OR p.averageRating >= :rating) " +
            "AND (:startPrice IS NULL OR :endPrice IS NULL OR r.price BETWEEN :startPrice AND :endPrice) " +
            "AND (:isBreakfast IS NULL OR r.includeBreakfast = :isBreakfast) " +
            "AND (:city IS NULL OR p.city.name = :city) " +
            "AND (:category IS NULL OR pc.name = :category)")
    Page<PropertyProjection> findFilteredPropertiesWithPrice(
            @Param("rating") Double rating,
            @Param("startPrice") Double startPrice,
            @Param("endPrice") Double endPrice,
            @Param("isBreakfast") Boolean isBreakfast,
            @Param("city") String city,
            @Param("category") String category,
            Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Properties p SET p.totalreview = (SELECT COUNT(r) FROM Review r WHERE r.properties = p)")
    void updateAllPropertiesTotalReviews();
}