package com.rooms.rooms.properties.repository;

import com.rooms.rooms.properties.dto.PropertyProjection;
import com.rooms.rooms.properties.entity.Properties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PropertiesRepository extends JpaRepository<Properties, Long> {
    Optional<Properties> findByName(String name);

    Properties findPropertiesBySlug(String slug);

    @Query("SELECT p FROM Properties p WHERE p.users.email = :userEmail")
    Optional<List<Properties>> findByUserEmail(@Param("userEmail") String userEmail);

    @Query("SELECT p FROM Properties p JOIN PropertyPicture pp ON pp.properties.id = p.id WHERE pp.id IN :pictureIds")
    Properties findPropertiesByPropertyPictureIds(@Param("pictureIds") Long pictureId);

    @Query("SELECT p FROM Properties p JOIN FETCH p.users WHERE p.id = :id")
    Optional<Properties> findByIdWithUsers(@Param("id") Long id);

    @Query(value = "SELECT DISTINCT p AS property, " +
            "COALESCE(MIN(r.price), 0) AS price, " +
            "CASE WHEN BOOL_OR(r.includeBreakfast) THEN true ELSE false END AS isBreakfast " +
            "FROM Properties p " +
            "LEFT JOIN p.rooms r " +
            "LEFT JOIN p.propertyCategories pc " +
            "WHERE p.deletedAt IS NULL " +
            "AND (:city IS NULL OR p.city.name = :city) " +
            "AND (:category IS NULL OR pc.name = :category) " +
            "AND (:rating IS NULL OR p.averageRating >= :rating) " +
            "AND (:startPrice IS NULL OR r.price >= :startPrice) " +
            "AND (:endPrice IS NULL OR r.price <= :endPrice) " +
            "AND (:isBreakfast IS NULL OR r.includeBreakfast = :isBreakfast) " +
            "GROUP BY p.id",
            countQuery = "SELECT COUNT(DISTINCT p.id) " +
                    "FROM Properties p " +
                    "LEFT JOIN p.rooms r " +
                    "LEFT JOIN p.propertyCategories pc " +
                    "WHERE p.deletedAt IS NULL " +
                    "AND (:city IS NULL OR p.city.name = :city) " +
                    "AND (:category IS NULL OR pc.name = :category) " +
                    "AND (:rating IS NULL OR p.averageRating >= :rating) " +
                    "AND (:startPrice IS NULL OR r.price >= :startPrice) " +
                    "AND (:endPrice IS NULL OR r.price <= :endPrice) " +
                    "AND (:isBreakfast IS NULL OR r.includeBreakfast = :isBreakfast)")
    Page<PropertyProjection> findFilteredPropertiesWithPrice(
            @Param("city") String city,
            @Param("category") String category,
            @Param("rating") Double rating,
            @Param("startPrice") Double startPrice,
            @Param("endPrice") Double endPrice,
            @Param("isBreakfast") Boolean isBreakfast,
            Pageable pageable);

//    @Query("SELECT p AS property, " +
//            "COALESCE(MIN(r.price) * (1 + COALESCE(MAX(CASE WHEN ps.startDate <= :checkOutDate AND ps.endDate >= :checkInDate " +
//            "                   THEN ps.markUpPercentage ELSE 0 END), 0) / 100), 0) AS price, " +
//            "CASE WHEN MAX(CASE WHEN r.includeBreakfast THEN 1 ELSE 0 END) > 0 THEN true ELSE false END AS isBreakfast " +
//            "FROM Properties p " +
//            "LEFT JOIN p.rooms r " +
//            "LEFT JOIN p.propertyCategories pc " +
//            "LEFT JOIN p.peakSeasons ps " +
//            "WHERE p.deletedAt IS NULL " +
//            "AND (:city IS NULL OR p.city.name = :city) " +
//            "AND (:category IS NULL OR pc.name = :category) " +
//            "AND (:rating IS NULL OR p.averageRating >= :rating) " +
//            "AND (:startPrice IS NULL OR r.price >= :startPrice) " +
//            "AND (:endPrice IS NULL OR r.price <= :endPrice) " +
//            "AND (:isBreakfast IS NULL OR r.includeBreakfast = :isBreakfast) " +
//            "GROUP BY p")
//    Page<PropertyProjection> findFilteredPropertiesWithPrice(
//            @Param("city") String city,
//            @Param("category") String category,
//            @Param("rating") Double rating,
//            @Param("startPrice") Double startPrice,
//            @Param("endPrice") Double endPrice,
//            @Param("isBreakfast") Boolean isBreakfast,
//            @Param("checkInDate") LocalDate checkInDate,
//            @Param("checkOutDate") LocalDate checkOutDate,
//            Pageable pageable);


    @Modifying
    @Transactional
    @Query("UPDATE Properties p SET p.totalReview = (SELECT COUNT(r) FROM Review r WHERE r.properties = p)")
    void updateAllPropertiesTotalReviews();
}