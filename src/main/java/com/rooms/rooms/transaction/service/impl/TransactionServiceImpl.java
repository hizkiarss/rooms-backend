package com.rooms.rooms.transaction.service.impl;

import com.rooms.rooms.exceptions.AlreadyExistException;
import com.rooms.rooms.exceptions.DataNotFoundException;
import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.properties.service.PropertiesService;
import com.rooms.rooms.transaction.dto.TransactionRequest;
import com.rooms.rooms.transaction.dto.TransactionResponse;
import com.rooms.rooms.transaction.entity.Transaction;
import com.rooms.rooms.transaction.repository.TransactionRepository;
import com.rooms.rooms.transaction.service.TransactionService;
import com.rooms.rooms.users.entity.Users;
import com.rooms.rooms.users.service.UsersService;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Log
@Service
public class TransactionServiceImpl implements TransactionService {
     private TransactionRepository transactionRepository;
     private UsersService usersService;
     private PropertiesService propertiesService;
     public TransactionServiceImpl(TransactionRepository transactionRepository, UsersService usersService, PropertiesService propertiesService){
          this.transactionRepository = transactionRepository;
          this.usersService = usersService;
          this.propertiesService = propertiesService;
     }

     @Override

     public String createTransaction(TransactionRequest req){
          Transaction newTransaction = req.toTransaction();
          Users users = usersService.getUsersById( req.getUsersId());
          Properties properties = propertiesService.getPropertiesById(req.getPropertiesId());
          newTransaction.setUsers(users);
          newTransaction.setProperties(properties);
          transactionRepository.save(newTransaction);
          return "Transaction created successfully";
     }

     @Override
     public TransactionResponse getTransactionById(Long id) {
          Optional<Transaction> transaction = transactionRepository.findById(id);
          if (!transaction.isPresent()) {
               throw new DataNotFoundException("Transaction with id " + id + " not found");
          }
          TransactionResponse transactionResponse = transaction.get().toTransactionResponse();
          transactionResponse.setUsers(transaction.get().getUsers());
          transactionResponse.setProperties(transaction.get().getProperties());
          return transactionResponse;
     }
}
