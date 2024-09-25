package com.rooms.rooms.transaction.dto;

import lombok.Data;

@Data
public class MonthlyTransactionsDto {
     private String month;
     private Integer totalTransactions;
}
