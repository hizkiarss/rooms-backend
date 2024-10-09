package com.rooms.rooms.review.dto;

import com.rooms.rooms.review.entity.Review;
import lombok.Data;

@Data
public class ReviewRequest {
     private String bookingCode;
     private Long propertyId;
     private Long userId;
     private String feedback;
     private Integer rating;

     public Review toReview() {
          Review review = new Review();
          review.setFeedback(feedback);
          review.setRating(rating);
          return review;
     }
}
