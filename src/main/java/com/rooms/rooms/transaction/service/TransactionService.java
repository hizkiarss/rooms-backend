package com.rooms.rooms.transaction.service;

import com.rooms.rooms.transaction.dto.TransactionRequest;
import com.rooms.rooms.transaction.dto.TransactionResponse;
import com.rooms.rooms.transaction.entity.Transaction;
import com.rooms.rooms.transaction.entity.TransactionStatus;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public interface TransactionService {
     String createTransaction(TransactionRequest req);
     String updateTransactionStatus(Long transactionId, TransactionStatus status);
     String cancelTransaction(String bookingCode);
     String expireTransaction(String bookingCode);
     TransactionResponse getTransactionResponseById(Long id);
     Transaction getTransactionById(Long id);
     Transaction getTransactionByBookingCode(String bookingCode);
     List<TransactionResponse> getTransactionByStatus(TransactionStatus status);
     List<TransactionResponse> getTransactionByUsersId(Long id);
     List<TransactionResponse> getTransactionByPropertyId(Long id);
     List<TransactionResponse> getAllTransaction();
     TransactionResponse getTransactionResponseByBookingCode(String bookingCode);
     void acceptTransaction(String bookingCode, String signature);
     void pendingTransaction(String bookingCode);
     void sendCheckInReminder();
     void checkPendingTransactions();
}
