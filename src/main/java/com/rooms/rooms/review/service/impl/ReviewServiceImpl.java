package com.rooms.rooms.review.service.impl;

import com.rooms.rooms.exceptions.AlreadyExistException;
import com.rooms.rooms.exceptions.DataNotFoundException;
import com.rooms.rooms.exceptions.OrderNotCompletedException;
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

     public ReviewServiceImpl(ReviewRepository reviewRepository, TransactionService transactionService, PropertiesService propertiesService, UsersService usersService) {
          this.reviewRepository = reviewRepository;
          this.transactionService = transactionService;
          this.propertiesService = propertiesService;
          this.usersService = usersService;
     }

     @Override
     public String createReview(ReviewRequest reviewRequest) {
          Transaction transaction = transactionService.getTransactionByBookingCode(reviewRequest.getBookingCode());
          Users users = usersService.getUsersById(reviewRequest.getUserId());
          Properties properties = propertiesService.getPropertiesById(reviewRequest.getPropertyId());
          Boolean isExist = reviewRepository.existsByTransactionIdAndUsersId(transaction.getId(), users.getId());

          if (isExist) {
               throw new AlreadyExistException("Review already exists");
          }

          if(transaction.getStatus() != TransactionStatus.Success){
               throw new OrderNotCompletedException("Order not completed");
          }

          if (transaction.getTransactionDetails().get(0).getEndDate().isAfter(LocalDate.now())) {
               throw new OrderNotCompletedException("Order not completed. You can only review after the end date.");
          }

          Review review = reviewRequest.toReview();
          review.setUsers(users);
          review.setProperties(properties);
          review.setTransaction(transaction);
          reviewRepository.save(review);
          return "Review created successfully";
     }

     @Override
     public List<Review> getReviewByPropertyId(Long propertyId) {
         List<Review> reviews = reviewRepository.findAllByPropertiesId(propertyId);
          if (reviews.isEmpty())  {
               throw new DataNotFoundException("Review  with property id " + propertyId + " not found");
          }
          return reviews;
     }
}
