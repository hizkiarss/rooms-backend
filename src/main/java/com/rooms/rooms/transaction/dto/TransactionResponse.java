package com.rooms.rooms.transaction.dto;

import com.rooms.rooms.paymentProof.entity.PaymentProof;
import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.review.entity.Review;
import com.rooms.rooms.status.entity.Status;
import com.rooms.rooms.transaction.entity.TransactionPaymentMethod;
import com.rooms.rooms.transaction.entity.TransactionStatus;
import com.rooms.rooms.transactionDetail.entity.TransactionDetail;
import com.rooms.rooms.users.entity.Users;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class TransactionResponse {

     private Long id;
     private Users users;
     private Properties properties;
     private Double finalPrice;
     private Double tax;
     private Integer adult;
     private Integer children;
     private TransactionStatus status;
     private TransactionPaymentMethod paymentMethod;
     private String firstName;
     private String lastName;
     private String mobileNumber;
     private String bookingCode;
     private List<TransactionDetail> transactionDetails;
     private List<PaymentProof> paymentProofs;
     private List<Review> reviews;
     private Instant createdAt;
}
