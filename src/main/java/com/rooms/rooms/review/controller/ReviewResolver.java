package com.rooms.rooms.review.controller;

import com.rooms.rooms.helper.JwtClaims;
import com.rooms.rooms.review.dto.ReviewRequest;
import com.rooms.rooms.review.entity.Review;
import com.rooms.rooms.review.entity.ReviewPage;
import com.rooms.rooms.review.service.ReviewService;
import lombok.extern.java.Log;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Log
@Controller
public class ReviewResolver {
     private ReviewService reviewService;

     public ReviewResolver(ReviewService reviewService) {
          this.reviewService = reviewService;
     }

     @PreAuthorize("hasAuthority('SCOPE_USER')")
     @MutationMapping(value = "createReview")
     public String createReview(@Argument("input") ReviewRequest reviewRequest) {
          return reviewService.createReview(reviewRequest);
     }

     //     @QueryMapping(value = "reviewByPropertyId")
//     public List<Review> getReviewByPropertyId(@Argument("propertyId") Long propertyId) {
//          return reviewService.getReviewByPropertyId(propertyId);
//     }
     @QueryMapping(value = "reviewByPropertyId")
     public ReviewPage getReviewByPropertyId(
             @Argument("propertyId") Long propertyId,
             @Argument("page") int page,
             @Argument("size") int size,
             @Argument("sortBy") String sortBy) {

          List<Review> reviews = reviewService.getReviewByPropertyId(propertyId, page, size, sortBy);
          long totalItems = reviewService.getTotalReviewCountByPropertyId(propertyId);
          int totalPages = (int) Math.ceil((double) totalItems / size);

          return new ReviewPage(reviews, totalPages, totalItems);
     }

     @PreAuthorize("hasAuthority('SCOPE_TENANT')")
     @QueryMapping(value = "unRepliedReviewByPropertyId")
     public List<Review> getUnRepliedReviewByPropertyId(@Argument("propertyId") Long propertyId) {
          return reviewService.getUnRepliedReviewByPropertyId(propertyId);
     }

     @PreAuthorize("hasAuthority('SCOPE_TENANT')")
     @QueryMapping(value = "unReadReviewByPropertyId")
     public List<Review> getUnReadReviewByPropertyId(@Argument("propertyId") Long propertyId) {
          return reviewService.getUnReadReviewByPropertyId(propertyId);
     }

     @PreAuthorize("hasAuthority('SCOPE_TENANT')")
     @MutationMapping(value = "replyReview")
     public String replyReview(@Argument("reviewId") Long reviewId, @Argument("reply") String reply) {
          return reviewService.replyReview(reviewId, reply);
     }

     @PreAuthorize("hasAuthority('SCOPE_TENANT')")
     @MutationMapping(value = "setReadReview")
     public String setReadReview(@Argument("reviewId") Long reviewId) {
          return reviewService.setRead(reviewId);
     }


}
