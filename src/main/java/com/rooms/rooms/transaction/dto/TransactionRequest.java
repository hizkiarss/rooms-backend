package com.rooms.rooms.transaction.dto;

import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.transaction.entity.Transaction;
import com.rooms.rooms.transaction.entity.TransactionPaymentMethod;
import com.rooms.rooms.transaction.entity.TransactionStatus;
import com.rooms.rooms.transactionDetail.dto.TransactionDetailRequest;
import com.rooms.rooms.users.entity.Users;
import lombok.Data;

import java.util.List;

@Data
public class TransactionRequest {
     private Long usersId;
     private Long propertiesId;
     private TransactionPaymentMethod paymentMethod;
     private String firstName;
     private String lastName;
     private String mobileNumber;
     private Integer adult;
     private Integer children;
     private TransactionDetailRequest transactionDetailRequests;

     public Transaction toTransaction() {
          Transaction transaction = new Transaction();
          transaction.setPaymentMethod(this.paymentMethod);
          transaction.setFirstName(this.firstName);
          transaction.setLastName(this.lastName);
          transaction.setMobileNumber(this.mobileNumber);
          transaction.setAdult(this.adult);
          transaction.setChildren(this.children);
          return transaction;
     }
}
