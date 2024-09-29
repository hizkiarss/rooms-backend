package com.rooms.rooms.payment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rooms.rooms.payment.entity.*;
import com.rooms.rooms.payment.service.PaymentService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
public class PaymentResolver {
     private final PaymentService paymentService;
     private final ObjectMapper objectMapper;
     public PaymentResolver(PaymentService paymentService, ObjectMapper objectMapper) {
          this.paymentService = paymentService;
          this.objectMapper = objectMapper;
     }

     @PreAuthorize("hasAuthority('SCOPE_USER')")
     @MutationMapping(value = "createPayment")
     public PaymentResponse createPayment(@Argument("input") PaymentRequest input) {
          String jsonResponse = paymentService.createTransaction(input);

          try {
               return objectMapper.readValue(jsonResponse, PaymentResponse.class);
          } catch (JsonProcessingException e) {
               throw new RuntimeException("Error parsing JSON response", e);
          }
     }

     @PreAuthorize("hasAuthority('SCOPE_USER')")
     @MutationMapping(value = "createVirtualAccountCode")
     public PaymentResponse createAndSaveVirtualAccount(@Argument String bookingCode, @Argument String bank) {
          String jsonResponse = paymentService.createVirtualAccountCode(bookingCode, bank);
          try {
               return objectMapper.readValue(jsonResponse, PaymentResponse.class);
          } catch (JsonProcessingException e) {
               throw new RuntimeException("Error parsing JSON response", e);
          }
          //return createAndSaveVirtualAccount(bookingCode, bank);
     }

     @PreAuthorize("hasAuthority('SCOPE_USER')")
     @MutationMapping(value = "createPaymentInitial")
     public String createPaymentInitial(@Argument("input") PaymentInitial input) {
          return paymentService.createPaymentInitial(input);
     }

     @PreAuthorize("isAuthenticated()")
     @QueryMapping(value = "paymentByBookingCode")
     public Payment getPaymentByBookingCode(@Argument("bookingCode") String bookingCode) {
          return paymentService.getPaymentByBookingCode(bookingCode);
     }

}
