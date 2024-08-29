package com.rooms.rooms.transactionDetail.service;

import com.rooms.rooms.transactionDetail.dto.TransactionDetailRequest;
import com.rooms.rooms.transactionDetail.entity.TransactionDetail;

public interface TransactionDetailService {
     void addTransactionDetail(TransactionDetailRequest transactionDetailRequest);
}
