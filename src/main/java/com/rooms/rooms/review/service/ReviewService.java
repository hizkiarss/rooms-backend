package com.rooms.rooms.review.service;

import com.rooms.rooms.review.dto.ReviewRequest;
import com.rooms.rooms.review.entity.Review;

public interface ReviewService {
     String createReview(ReviewRequest reviewRequest);
}
