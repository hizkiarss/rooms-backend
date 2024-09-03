package com.rooms.rooms.review.controller;

import com.rooms.rooms.review.dto.ReviewRequest;
import com.rooms.rooms.review.entity.Review;
import com.rooms.rooms.review.service.ReviewService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ReviewResolver {
     private  ReviewService reviewService;
     public ReviewResolver(ReviewService reviewService) {
          this.reviewService = reviewService;
     }

     @MutationMapping(value = "createReview")
     public String createReview(@Argument("input") ReviewRequest reviewRequest) {
          return reviewService.createReview(reviewRequest);
     }

     @QueryMapping(value = "reviewByPropertyId")
     public List<Review> getReviewByPropertyId(@Argument("propertyId") Long propertyId) {
          return reviewService.getReviewByPropertyId(propertyId);
     }

     @QueryMapping(value = "unRepliedReviewByPropertyId")
     public List<Review> getUnRepliedReviewByPropertyId(@Argument("propertyId") Long propertyId) {
          return reviewService.getUnRepliedReviewByPropertyId(propertyId);
     }

}
