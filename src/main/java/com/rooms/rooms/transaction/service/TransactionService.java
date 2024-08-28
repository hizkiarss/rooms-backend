package com.rooms.rooms.transaction.service;

import com.rooms.rooms.transaction.dto.TransactionRequest;
import com.rooms.rooms.transaction.dto.TransactionResponse;
import com.rooms.rooms.transaction.entity.Transaction;
import com.rooms.rooms.transaction.entity.TransactionStatus;

import java.util.List;

public interface TransactionService {
     String createTransaction(TransactionRequest req);
     String updateTransactionStatus(Long transactionId, TransactionStatus status);
     TransactionResponse getTransactionResponseById(Long id);
     Transaction getTransactionById(Long id);
     List<TransactionResponse> getTransactionByStatus(TransactionStatus status);
     List<TransactionResponse> getTransactionByUsersId(Long id);
     List<TransactionResponse> getTransactionByPropertyId(Long id);
     List<TransactionResponse> getAllTransaction();
}
