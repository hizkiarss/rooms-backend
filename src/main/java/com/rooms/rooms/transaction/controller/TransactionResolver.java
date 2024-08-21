package com.rooms.rooms.transaction.controller;

import com.rooms.rooms.transaction.dto.TransactionRequest;
import com.rooms.rooms.transaction.dto.TransactionResponse;
import com.rooms.rooms.transaction.service.TransactionService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class TransactionResolver {
     private TransactionService transactionService;
     public TransactionResolver(TransactionService transactionService){
          this.transactionService = transactionService;
     }

     @QueryMapping(value = "hello")
     public String sayHello(){
          return "Hallo Gaes!!!";
     }

     @QueryMapping(value = "transactionById")
     public TransactionResponse getTransactionById(@Argument Long id){
          return transactionService.getTransactionById(id);
     }
     @QueryMapping(value = "transactions")
     public List<TransactionResponse> getTransactions(){
          return transactionService.getAllTransaction();
     }

     @QueryMapping(value = "transactionsByStatusId")
     public List<TransactionResponse> getTransactionByStatusId(@Argument Long statusId){
          return transactionService.getTransactionByStatusId(statusId);
     }

     @QueryMapping(value = "transactionsByUsersId")
     public List<TransactionResponse> getTransactionByUsersId(@Argument Long usersId){
          return transactionService.getTransactionByUsersId(usersId);
     }

     @QueryMapping(value = "transactionsByPropertyId")
     public List<TransactionResponse> getTransactionByPropertyId(@Argument Long propertyId){
          return transactionService.getTransactionByPropertyId(propertyId);
     }

     @MutationMapping(value = "createTransaction")
     public String createTransaction(@Argument("input") TransactionRequest input) {
          return transactionService.createTransaction(input);
     }
}
