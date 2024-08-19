package com.rooms.rooms.transaction.service;

import com.rooms.rooms.transaction.dto.TransactionRequest;
import com.rooms.rooms.transaction.dto.TransactionResponse;

public interface TransactionService {
     void createTransaction(TransactionRequest req);
     TransactionResponse getTransactionById(Long id);
}
