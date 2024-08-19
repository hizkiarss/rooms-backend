package com.rooms.rooms.transaction.controller;

import com.rooms.rooms.transaction.dto.TransactionRequest;
import com.rooms.rooms.transaction.dto.TransactionResponse;
import com.rooms.rooms.transaction.service.TransactionService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

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

     @QueryMapping(value = "transaction")
     public TransactionResponse getTransactionById(@Argument Long id){
          return transactionService.getTransactionById(id);
     }
     @MutationMapping(value = "createTransaction")
     public String createTransaction(@Argument("input") TransactionRequest input) {
          return transactionService.createTransaction(input);
     }


}
