package com.rooms.rooms.transactionDetail.dto;

import com.rooms.rooms.transactionDetail.entity.TransactionDetail;
import lombok.Data;

import java.time.LocalDate;
@Data
public class TransactionDetailRequest {
     private Long transactionId;
     private Long roomId;
     private LocalDate startDate;
     private LocalDate endDate;

     public TransactionDetail toTransactionDetail() {
          TransactionDetail transactionDetail = new TransactionDetail();
          transactionDetail.setStartDate(this.startDate);
          transactionDetail.setEndDate(this.endDate);
          return transactionDetail;
     }
}
