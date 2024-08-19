package com.rooms.rooms.transaction.dto;

import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.users.entity.Users;
import lombok.Data;

@Data
public class TransactionRequest {
     private Long usersId;
     private Long propertiesId;
     private Double finalPrice;
     private String status;
     private String paymentMethod;
}
