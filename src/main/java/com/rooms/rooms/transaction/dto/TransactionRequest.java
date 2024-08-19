package com.rooms.rooms.transaction.dto;

import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.transaction.entity.Transaction;
import com.rooms.rooms.users.entity.Users;
import lombok.Data;

@Data
public class TransactionRequest {
     private Long usersId;
     private Long propertiesId;
     private Double finalPrice;
     private Integer status;
     private String paymentMethod;

     public Transaction toTransaction(){
          Transaction transaction = new Transaction();
          transaction.setFinalPrice(this.finalPrice);
          transaction.setPaymentMethod(this.paymentMethod);
          return transaction;
     }
}
