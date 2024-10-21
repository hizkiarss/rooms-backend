package com.rooms.rooms.review.service.impl;

import com.rooms.rooms.exceptions.AlreadyExistException;
import com.rooms.rooms.exceptions.DataNotFoundException;
import com.rooms.rooms.exceptions.OrderNotCompletedException;
import com.rooms.rooms.helper.CurrentUser;
import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.properties.service.PropertiesService;
import com.rooms.rooms.review.dto.ReviewRequest;
import com.rooms.rooms.review.entity.Review;
import com.rooms.rooms.review.repository.ReviewRepository;
import com.rooms.rooms.review.service.ReviewService;
import com.rooms.rooms.transaction.entity.Transaction;
import com.rooms.rooms.transaction.entity.TransactionStatus;
import com.rooms.rooms.transaction.service.TransactionService;
import com.rooms.rooms.users.entity.Users;
import com.rooms.rooms.users.service.UsersService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {
    private ReviewRepository reviewRepository;
    private TransactionService transactionService;
    private PropertiesService propertiesService;
    private UsersService usersService;
    private CurrentUser currentUser;

    public ReviewServiceImpl(ReviewRepository reviewRepository, @Lazy TransactionService transactionService, PropertiesService propertiesService, UsersService usersService, CurrentUser currentUser) {
        this.reviewRepository = reviewRepository;
        this.transactionService = transactionService;
        this.propertiesService = propertiesService;
        this.usersService = usersService;
        this.currentUser = currentUser;
    }

    @Override
    public String createReview(ReviewRequest reviewRequest) {
        Transaction transaction = transactionService.getTransactionByBookingCode(reviewRequest.getBookingCode());
        Long authorizedUserId = currentUser.getCurrentUserId();
        Users users = usersService.getUsersById(authorizedUserId);
        Properties properties = propertiesService.getPropertiesById(reviewRequest.getPropertyId());
        Boolean isExist = reviewRepository.existsByTransactionIdAndUsersId(transaction.getId(), users.getId());

        if (isExist) {
            throw new AlreadyExistException("Review already exists");
        }

        if (transaction.getStatus() != TransactionStatus.Success) {
            throw new OrderNotCompletedException("Order not completed");
        }

        if (transaction.getTransactionDetails().get(0).getEndDate().isAfter(LocalDate.now())) {
            throw new OrderNotCompletedException("Order not completed. You can only review after the end date.");
        }

        Review review = reviewRequest.toReview();
        review.setUsers(users);
        review.setProperties(properties);
        review.setTransaction(transaction);
        review.setIsRead(false);
        reviewRepository.save(review);
        return "Review created successfully";
    }

    @Override
    public List<Review> getReviewByPropertyId(Long propertyId, int page, int size, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        if (sortBy.equals("HIGHEST_RATING")) {
            sort = Sort.by(Sort.Direction.DESC, "rating");
        } else if (sortBy.equals("LOWEST_RATING")) {
            sort = Sort.by(Sort.Direction.ASC, "rating");
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Review> reviewPage = reviewRepository.findAllByPropertiesId(propertyId, pageable);

        if (reviewPage.isEmpty()) {
            throw new DataNotFoundException("No reviews found for property id " + propertyId);
        }

        return reviewPage.getContent();
    }

    @Override
    public Integer getTotalReviewCountByPropertyId(Long propertyId){
        return reviewRepository.countByPropertiesId(propertyId);
    }

    @Override
    public List<Review> getUnRepliedReviewByPropertyId(Long propertyId) {
        List<Review> reviews = reviewRepository.findAllByPropertiesIdAndReplyIsNull(propertyId);
        if (reviews.isEmpty()) {
            throw new DataNotFoundException("Review  with property id " + propertyId + " not found");
        }
        return reviews;
    }

    @Override
    public List<Review> getUnReadReviewByPropertyId(Long propertyId) {
        List<Review> reviews = reviewRepository.findAllByPropertiesIdAndIsReadFalseAndReplyIsNull(propertyId);
        if (reviews.isEmpty()) {
            throw new DataNotFoundException("Review  with property id " + propertyId + " not found");
        }
        return reviews;
    }

    @Override
    public String replyReview(Long reviewId, String reply) {
        Review review = reviewRepository.findByIdAndReplyIsNull(reviewId);
        if (review == null)
            throw new DataNotFoundException("Review  with id " + reviewId + " not found / already have reply");
        review.setReply(reply);
        review.setIsRead(true);
        reviewRepository.save(review);
        return "Review replied successfully";
    }

    @Override
    public String setRead(Long reviewId) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        if (review.isEmpty() || review == null) {
            throw new DataNotFoundException("Review  with id " + reviewId + " not found");
        }
        review.get().setIsRead(true);
        reviewRepository.save(review.get());
        return "Review read successfully";
    }

    @Override
    public List<Review> findReviewbyProperties(Properties properties) {
        return reviewRepository.findAllByProperties(properties);
    }
}
