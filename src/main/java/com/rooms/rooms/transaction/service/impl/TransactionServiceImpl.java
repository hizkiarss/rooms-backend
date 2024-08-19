package com.rooms.rooms.transaction.service.impl;

import com.rooms.rooms.transaction.dto.TransactionRequest;
import com.rooms.rooms.transaction.dto.TransactionResponse;
import com.rooms.rooms.transaction.entity.Transaction;
import com.rooms.rooms.transaction.repository.TransactionRepository;
import com.rooms.rooms.transaction.service.TransactionService;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class TransactionServiceImpl implements TransactionService {
     private TransactionRepository transactionRepository;
     public TransactionServiceImpl(TransactionRepository transactionRepository){
          this.transactionRepository = transactionRepository;
     }

     @Override
     public void createTransaction(TransactionRequest req){

     }

     @Override
     public TransactionResponse getTransactionById(Long id) {
          Optional<Transaction> transaction = transactionRepository.findById(id);
          if (!transaction.isPresent()) {
               throw new RuntimeException("Transaction with id " + id + " not found");
          }
          TransactionResponse transactionResponse = transaction.get().toTransactionResponse();
          transactionResponse.setUsers(transaction.get().getUsers());
          transactionResponse.setProperties(transaction.get().getProperties());
          return transactionResponse;
     }
}
