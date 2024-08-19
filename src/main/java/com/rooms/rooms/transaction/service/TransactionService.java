package com.rooms.rooms.transaction.service;

import com.rooms.rooms.transaction.dto.TransactionRequest;
import com.rooms.rooms.transaction.dto.TransactionResponse;

import java.util.List;

public interface TransactionService {
     String createTransaction(TransactionRequest req);
     TransactionResponse getTransactionById(Long id);
     List<TransactionResponse> getTransactionByStatusId(Long id);
     List<TransactionResponse> getTransactionByUsersId(Long id);
     List<TransactionResponse> getTransactionByPropertyId(Long id);
     List<TransactionResponse> getAllTransaction();
}
