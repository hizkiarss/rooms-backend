package com.rooms.rooms.transaction.service.impl;

import com.rooms.rooms.exceptions.AlreadyExistException;
import com.rooms.rooms.exceptions.DataNotFoundException;
import com.rooms.rooms.helper.StringGenerator;
import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.properties.service.PropertiesService;
import com.rooms.rooms.rooms.entity.Rooms;
import com.rooms.rooms.rooms.service.RoomsService;
import com.rooms.rooms.status.entity.Status;
import com.rooms.rooms.status.service.StatusService;
import com.rooms.rooms.transaction.dto.TransactionRequest;
import com.rooms.rooms.transaction.dto.TransactionResponse;
import com.rooms.rooms.transaction.entity.Transaction;
import com.rooms.rooms.transaction.entity.TransactionStatus;
import com.rooms.rooms.transaction.repository.TransactionRepository;
import com.rooms.rooms.transaction.service.TransactionService;
import com.rooms.rooms.transactionDetail.dto.TransactionDetailRequest;
import com.rooms.rooms.transactionDetail.service.TransactionDetailService;
import com.rooms.rooms.users.entity.Users;
import com.rooms.rooms.users.service.UsersService;
import lombok.extern.java.Log;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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
     private TransactionDetailService transactionDetailService;
     private RoomsService roomsService;

     public TransactionServiceImpl(
             TransactionRepository transactionRepository,
             UsersService usersService,
             PropertiesService propertiesService,
             StatusService statusService,
             @Lazy TransactionDetailService transactionDetailService,
             @Lazy RoomsService roomsService) {
          this.transactionRepository = transactionRepository;
          this.usersService = usersService;
          this.propertiesService = propertiesService;
          this.statusService = statusService;
          this.transactionDetailService = transactionDetailService;
          this.roomsService = roomsService;
     }

     @Override
     @Transactional
     public String createTransaction(TransactionRequest req) {
          Transaction newTransaction = req.toTransaction();
          Users users = usersService.getUsersById(req.getUsersId());
          Properties properties = propertiesService.getPropertiesById(req.getPropertiesId());
          TransactionDetailRequest transactionDetailRequest =  req.getTransactionDetailRequests();
          Rooms rooms = roomsService.getRoomsById(req.getTransactionDetailRequests().getRoomId());
          Double price = rooms.getPrice();
          String bookingCode = StringGenerator.generateRandomString(8);
          newTransaction.setFinalPrice(price);
          newTransaction.setUsers(users);
          newTransaction.setProperties(properties);
          newTransaction.setStatus(TransactionStatus.Pending);
          newTransaction.setBookingCode(bookingCode);
          Transaction savedTransaction = transactionRepository.save(newTransaction);
          transactionDetailRequest.setTransactionId(savedTransaction.getId());
          transactionDetailService.addTransactionDetail(transactionDetailRequest);
          return bookingCode ;
     }

     @Override
     public TransactionResponse getTransactionResponseById(Long id) {
          Optional<Transaction> transaction  = Optional.ofNullable(transactionRepository.findByIdAndDeletedAtIsNull(id));
          if(transaction.isEmpty()){
               throw new DataNotFoundException("Transaction with id " + id + " not found");
          }
          TransactionResponse transactionResponse = transaction.get().toTransactionResponse();
          transactionResponse.setUsers(transaction.get().getUsers());
          transactionResponse.setProperties(transaction.get().getProperties());
          transactionResponse.setStatus(transaction.get().getStatus());
          transactionResponse.setTransactionDetails(transaction.get().getTransactionDetails());
          return transactionResponse;
     }

     @Override
     public TransactionResponse getTransactionResponseByBookingCode(String bookingCode) {
          Optional<Transaction> transaction = Optional.ofNullable(transactionRepository.findByBookingCodeAndDeletedAtIsNull(bookingCode));
          if(transaction.isEmpty() || transaction == null){
               throw new DataNotFoundException("Transaction with booking code " + bookingCode + " not found");
          }
          TransactionResponse transactionResponse = transaction.get().toTransactionResponse();
          transactionResponse.setUsers(transaction.get().getUsers());
          transactionResponse.setProperties(transaction.get().getProperties());
          transactionResponse.setStatus(transaction.get().getStatus());
          transactionResponse.setTransactionDetails(transaction.get().getTransactionDetails());
          return transactionResponse;
     }

     @Override
     public Transaction getTransactionById(Long id) {
          Optional<Transaction> transaction  = Optional.ofNullable(transactionRepository.findByIdAndDeletedAtIsNull(id));
          if(transaction.isEmpty()){
               throw new DataNotFoundException("Transaction with id " + id + " not found");
          }
          return transaction.orElse(null );
     }

     @Override
     public String updateTransactionStatus(Long transactionId, TransactionStatus status){
          Optional<Transaction> transaction  = Optional.ofNullable(transactionRepository.findByIdAndDeletedAtIsNull(transactionId));
          if(transaction.isEmpty() || transaction == null){
               throw new DataNotFoundException("Transaction with id " + transactionId + " not found");
          }
          transaction.get().setStatus(status);
          transactionRepository.save(transaction.get());
          return "Transaction updated successfully";
     }

     @Override
     public List<TransactionResponse> getTransactionByStatus(TransactionStatus status) {
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
              // throw new DataNotFoundException("Transaction with Status id  " + id + " not found");
               return Collections.emptyList();
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
          transactionResponse.setTransactionDetails(transaction.getTransactionDetails());
          return transactionResponse;
     }
}
