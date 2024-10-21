package com.rooms.rooms.review.service;

import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.review.dto.ReviewRequest;
import com.rooms.rooms.review.entity.Review;

import java.util.List;

public interface ReviewService {
     String createReview(ReviewRequest reviewRequest);

     List<Review> getReviewByPropertyId(Long propertyId, int page, int size, String sortBy);

     Integer getTotalReviewCountByPropertyId(Long propertyId);

     List<Review> getUnRepliedReviewByPropertyId(Long propertyId);

     List<Review> getUnReadReviewByPropertyId(Long propertyId);

     String replyReview(Long reviewId, String reply);

     String setRead(Long reviewId);

     List<Review> findReviewbyProperties(Properties properties);
}
