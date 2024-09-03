package com.rooms.rooms.review.repository;

import com.rooms.rooms.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
     List<Review> findAllByPropertiesId(Long propertyId);
     Boolean existsByTransactionIdAndUsersId(Long transactionId, Long userId);
}
