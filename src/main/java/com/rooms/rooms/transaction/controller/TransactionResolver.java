package com.rooms.rooms.transaction.controller;

import com.rooms.rooms.Responses.PageResponse;
import com.rooms.rooms.helper.CurrentUser;
import com.rooms.rooms.helper.JwtClaims;
import com.rooms.rooms.transaction.dto.MonthlyTransactionsDto;
import com.rooms.rooms.transaction.dto.TransactionRequest;
import com.rooms.rooms.transaction.dto.TransactionResponse;
import com.rooms.rooms.transaction.entity.Transaction;
import com.rooms.rooms.transaction.entity.TransactionPage;
import com.rooms.rooms.transaction.entity.TransactionStatus;
import com.rooms.rooms.transaction.service.TransactionService;
import lombok.extern.java.Log;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Log
@Controller
public class TransactionResolver {
     private TransactionService transactionService;
     private CurrentUser currentUser;

     public TransactionResolver(TransactionService transactionService, CurrentUser currentUser) {
          this.transactionService = transactionService;
          this.currentUser = currentUser;
     }

     @QueryMapping(value = "hello")
     public String sayHello() {
          return "Hallo gaes!!!";
     }


     @PreAuthorize("hasAuthority('SCOPE_TENANT')")
     @QueryMapping(value = "transactionsByStatus")
     public List<TransactionResponse> getTransactionByStatus(@Argument TransactionStatus status) {
          return transactionService.getTransactionByStatus(status);
     }


     @PreAuthorize("hasAuthority('SCOPE_USER')")
     @QueryMapping(value = "transactionsByUsersId")
     public TransactionPage getTransactionByUsersId(
             @Argument int page,
             @Argument int size,
             @Argument String status,
             @Argument String sort) {

          Long usersId = currentUser.getCurrentUserId();
          TransactionStatus transactionStatus = null;
          if (status != null) {
               try {
                    transactionStatus = TransactionStatus.valueOf(status);
               } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid transaction status: " + status);
               }
          }

          PageResponse<TransactionResponse> pageResponse = transactionService.getTransactionByUsersId(
                  usersId, page, size, transactionStatus, sort);

          return new TransactionPage(pageResponse);
     }

     @PreAuthorize("isAuthenticated()")
     @QueryMapping(value = "transactionsByBookingCode")
     public TransactionResponse getTransactionByBookingCode(@Argument String bookingCode) {
          return transactionService.getTransactionResponseByBookingCode(bookingCode);
     }

     @PreAuthorize("hasAuthority('SCOPE_TENANT')")
     @QueryMapping(value = "transactionsByPropertyId")
     public List<TransactionResponse> getTransactionByPropertyId(@Argument Long propertyId, @Argument LocalDate startDate, @Argument LocalDate endDate) {
          return transactionService.getTransactionByPropertyId(propertyId, startDate, endDate);
     }

     @PreAuthorize("hasAuthority('SCOPE_USER')")
     @MutationMapping(value = "createTransaction")
     public String createTransaction(@Argument("input") TransactionRequest input) {

          return transactionService.createTransaction(input);
     }

     @PreAuthorize("hasAuthority('SCOPE_USER')")
     @MutationMapping(value = "cancelTransaction")
     public String cancelTransaction(@Argument("bookingCode") String bookingCode) {
          return transactionService.cancelTransaction(bookingCode);
     }

     @PreAuthorize("hasAuthority('SCOPE_TENANT')")
     @QueryMapping(value = "revenueByProperty")
     public BigDecimal getRevenueByProperty(@Argument Long propertyId, @Argument LocalDate startDate, @Argument LocalDate endDate) {
          return transactionService.getTotalRevenueByPropertyId(propertyId, startDate, endDate);
     }

     @PreAuthorize("hasAuthority('SCOPE_TENANT')")
     @QueryMapping(value = "taxByProperty")
     public BigDecimal getTaxByProperty(@Argument Long propertyId, @Argument LocalDate startDate, @Argument LocalDate endDate) {
          return transactionService.getTotalTaxByPropertyId(propertyId, startDate, endDate);
     }

     @PreAuthorize("hasAuthority('SCOPE_TENANT')")
     @QueryMapping(value = "revenueWithTaxByProperty")
     public BigDecimal getRevenueWithTaxByProperty(@Argument Long propertyId, @Argument LocalDate startDate, @Argument LocalDate endDate) {
          return transactionService.getTotalRevenueWithTaxByPropertyId(propertyId, startDate, endDate);
     }

     @PreAuthorize("hasAuthority('SCOPE_TENANT')")
     @QueryMapping(value = "totalTransactionsByPropertyId")
     public Integer getTotalTransactionByPropertyId(@Argument Long propertyId, @Argument LocalDate startDate, @Argument LocalDate endDate) {
          return transactionService.getTotalTransactionsByPropertyId(propertyId, startDate, endDate);
     }

     @PreAuthorize("hasAuthority('SCOPE_TENANT')")
     @QueryMapping(value = "monthlyTransactionsByPropertyId")
     public List<MonthlyTransactionsDto> getMonthlyTransactions(@Argument Long propertyId) {
          return transactionService.getMonthlyTransactionsByPropertyId(propertyId);
     }

     @PreAuthorize("hasAuthority('SCOPE_TENANT')")
     @QueryMapping(value = "latestTransactionsByPropertyId")
     public List<Transaction> getLatestTransactionsByPropertyId(@Argument Long propertyId) {
          return transactionService.getLatestTransactionsByPropertyId(propertyId);
     }

     @QueryMapping(value = "sendEmailRemainder")
     public String sendEmailRemainder(@Argument Long propertyId) {
          transactionService.sendCheckInEmail();
          return "email sent" + propertyId;
     }

     @QueryMapping(value = "acceptTransactionEmail")
     public String acceptTransactionEmail(@Argument Long propertyId) {
          transactionService.acceptTransactionEmail();
          return "email accepted" + propertyId;
     }



}
