package com.rooms.rooms.transaction.dto;

import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.transaction.entity.Transaction;
import com.rooms.rooms.transaction.entity.TransactionPaymentMethod;
import com.rooms.rooms.transaction.entity.TransactionStatus;
import com.rooms.rooms.users.entity.Users;
import lombok.Data;

@Data
public class TransactionRequest {
     private Long usersId;
     private Long propertiesId;
     private Double finalPrice;
     private TransactionStatus status;
     private TransactionPaymentMethod paymentMethod;
     private String firstName;
     private String lastName;
     private String mobileNumber;

     public Transaction toTransaction(){
          Transaction transaction = new Transaction();
          transaction.setFinalPrice(this.finalPrice);
          transaction.setPaymentMethod(this.paymentMethod);
          transaction.setStatus(this.status);
          transaction.setFirstName(this.firstName);
          transaction.setLastName(this.lastName);
          transaction.setMobileNumber(this.mobileNumber);
          return transaction;
     }
}
