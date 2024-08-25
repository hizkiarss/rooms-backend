package com.rooms.rooms.transaction.service.impl;

import com.rooms.rooms.exceptions.AlreadyExistException;
import com.rooms.rooms.exceptions.DataNotFoundException;
import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.properties.service.PropertiesService;
import com.rooms.rooms.status.entity.Status;
import com.rooms.rooms.status.service.StatusService;
import com.rooms.rooms.transaction.dto.TransactionRequest;
import com.rooms.rooms.transaction.dto.TransactionResponse;
import com.rooms.rooms.transaction.entity.Transaction;
import com.rooms.rooms.transaction.repository.TransactionRepository;
import com.rooms.rooms.transaction.service.TransactionService;
import com.rooms.rooms.users.entity.Users;
import com.rooms.rooms.users.service.UsersService;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log
@Service
public class TransactionServiceImpl implements TransactionService {
     private TransactionRepository transactionRepository;
     private UsersService usersService;
     private PropertiesService propertiesService;
     private StatusService statusService;

     public TransactionServiceImpl(
             TransactionRepository transactionRepository,
             UsersService usersService,
             PropertiesService propertiesService,
             StatusService statusService) {
          this.transactionRepository = transactionRepository;
          this.usersService = usersService;
          this.propertiesService = propertiesService;
          this.statusService = statusService;
     }

     @Override
     @Transactional
     public String createTransaction(TransactionRequest req) {
          Transaction newTransaction = req.toTransaction();
          Users users = usersService.getUsersById(req.getUsersId());
          Properties properties = propertiesService.getPropertiesById(req.getPropertiesId());

          newTransaction.setUsers(users);
          newTransaction.setProperties(properties);

          transactionRepository.save(newTransaction);
          return "Transaction created successfully";
     }

     @Override
     public TransactionResponse getTransactionById(Long id) {
          Optional<Transaction> transaction  = Optional.ofNullable(transactionRepository.findByIdAndDeletedAtIsNull(id));
          if(transaction.isEmpty()){
               throw new DataNotFoundException("Transaction with id " + id + " not found");
          }
          TransactionResponse transactionResponse = transaction.get().toTransactionResponse();
          transactionResponse.setUsers(transaction.get().getUsers());
          transactionResponse.setProperties(transaction.get().getProperties());
          transactionResponse.setStatus(transaction.get().getStatus());
          return transactionResponse;
     }

     @Override
     public List<TransactionResponse> getTransactionByStatus(String status) {
          List<Transaction> transactions = transactionRepository.findAllByStatusAndDeletedAtIsNull(status);

          if (transactions == null || transactions.isEmpty()) {
               throw new DataNotFoundException("Transaction with Status  " + status + " not found");
          }

          return transactions.stream().map(this::toTransactionResponse).collect(Collectors.toList());
     }

     @Override
     public List<TransactionResponse> getTransactionByUsersId(Long id) {
          List<Transaction> transactions = transactionRepository.findAllByUsersIdAndDeletedAtIsNull(id);

          if (transactions == null || transactions.isEmpty()) {
               throw new DataNotFoundException("Transaction with Status id  " + id + " not found");
          }

          return transactions.stream().map(this::toTransactionResponse).collect(Collectors.toList());
     }

     @Override
     public List<TransactionResponse> getTransactionByPropertyId(Long id) {
          List<Transaction> transactions = transactionRepository.findAllByPropertiesIdAndDeletedAtIsNull(id);

          if (transactions == null || transactions.isEmpty()) {
               throw new DataNotFoundException("Transaction with Status id  " + id + " not found");
          }

          return transactions.stream().map(this::toTransactionResponse).collect(Collectors.toList());
     }

     @Override
     public List<TransactionResponse> getAllTransaction(){
          List<Transaction> transactions = transactionRepository.findAllByDeletedAtIsNull();
          return transactions.stream().map(this::toTransactionResponse).collect(Collectors.toList());
     }

     private TransactionResponse toTransactionResponse(Transaction transaction){
          TransactionResponse transactionResponse = new TransactionResponse();
          transactionResponse.setId(transaction.getId());
          transactionResponse.setPaymentMethod(transaction.getPaymentMethod());
          transactionResponse.setFinalPrice(transaction.getFinalPrice());
          transactionResponse.setUsers(transaction.getUsers());
          transactionResponse.setStatus(transaction.getStatus());
          transactionResponse.setProperties(transaction.getProperties());
          transactionResponse.setFirstName(transaction.getFirstName());
          transactionResponse.setLastName(transaction.getLastName());
          transactionResponse.setMobileNumber(transaction.getMobileNumber());
          return transactionResponse;
     }
}
