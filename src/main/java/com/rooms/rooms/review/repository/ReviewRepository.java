package com.rooms.rooms.review.repository;

import com.rooms.rooms.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
