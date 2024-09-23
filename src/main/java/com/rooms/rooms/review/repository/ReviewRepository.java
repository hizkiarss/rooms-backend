package com.rooms.rooms.review.repository;

import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
     List<Review> findAllByPropertiesId(Long propertyId);
     List<Review> findAllByPropertiesIdAndReplyIsNull(Long propertyId);
     List<Review> findAllByPropertiesIdAndIsReadFalseAndReplyIsNull(Long propertyId);
     Boolean existsByTransactionIdAndUsersId(Long transactionId, Long userId);
     Review findByIdAndReplyIsNull(Long id);
     List<Review> findAllByProperties(Properties properties);
}
