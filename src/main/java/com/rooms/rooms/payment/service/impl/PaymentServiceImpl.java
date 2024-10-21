package com.rooms.rooms.payment.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rooms.rooms.exceptions.DataNotFoundException;
import com.rooms.rooms.payment.entity.*;
import com.rooms.rooms.payment.repository.PaymentRepository;
import com.rooms.rooms.payment.service.PaymentService;
import com.rooms.rooms.transaction.dto.TransactionResponse;
import com.rooms.rooms.transaction.entity.Transaction;
import com.rooms.rooms.transaction.entity.TransactionStatus;
import com.rooms.rooms.transaction.service.TransactionService;
import jakarta.transaction.Transactional;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Log
@Service
public class PaymentServiceImpl implements PaymentService {
     private PaymentRepository paymentRepository;
     private TransactionService transactionService;
     private final ObjectMapper objectMapper;
     public PaymentServiceImpl(TransactionService transactionService, PaymentRepository paymentRepository, ObjectMapper objectMapper) {
          this.transactionService = transactionService;
          this.paymentRepository = paymentRepository;
          this.objectMapper = objectMapper;
     }
     @Value("${midtrans.server.key}")
     private String serverKey;

     @Value("${midtrans.api.url}")
     private String apiUrl;

     @Override
     public String createTransaction(PaymentRequest paymentRequest) {
          RestTemplate restTemplate = new RestTemplate();
          HttpHeaders headers = new HttpHeaders();
          headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((serverKey + ":").getBytes()));
          headers.set("Content-Type", "application/json");
          HttpEntity<PaymentRequest> request = new HttpEntity<>(paymentRequest, headers);
          ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);

          return response.getBody();
     }

     @Override
     @Transactional
     public String createVirtualAccountCode(String bookingCode, String bank){
          TransactionResponse transaction = transactionService.getTransactionResponseByBookingCode(bookingCode);

          String PaymentType = "bank_transfer";
          PaymentTransactionDetails paymentTransactionDetails = new PaymentTransactionDetails();
          paymentTransactionDetails.setOrder_id(transaction.getBookingCode());
          paymentTransactionDetails.setGross_amount(transaction.getFinalPrice().intValue());

          BankTransfer bankTransfer = new BankTransfer();
          bankTransfer.setBank(bank);

          CustomerDetails customerDetails = new CustomerDetails();
          customerDetails.setFirst_name(transaction.getFirstName());
          customerDetails.setLast_name(transaction.getLastName());
          customerDetails.setEmail(transaction.getUsers().getEmail());
          customerDetails.setPhone(transaction.getMobileNumber());

          List<ItemDetail> itemDetailsList = new ArrayList<>();
          ItemDetail itemDetail = new ItemDetail();
          itemDetail.setName(transaction.getProperties().getName());
          itemDetail.setQuantity(1);
          itemDetail.setPrice(transaction.getFinalPrice().intValue());
          itemDetailsList.add(itemDetail);

          CustomExpiry customExpiry = new CustomExpiry();
          customExpiry.setExpiry_duration(60);

          PaymentRequest paymentRequest = new PaymentRequest();
          paymentRequest.setPayment_type(PaymentType);
          paymentRequest.setTransaction_details(paymentTransactionDetails);
          paymentRequest.setBank_transfer(bankTransfer);
          paymentRequest.setCustomer_details(customerDetails);
          paymentRequest.setCustom_expiry(customExpiry);
          paymentRequest.setItem_details(itemDetailsList);

          return createTransaction(paymentRequest);
     }

     @Override
     public String createPaymentInitial(PaymentInitial paymentInitial) {
          TransactionResponse transaction = transactionService.getTransactionResponseByBookingCode(paymentInitial.getBookingCode());
          Payment payment = paymentInitial.toPayment();
          payment.setGrossAmount(transaction.getFinalPrice());
          payment.setTransactionStatus(String.valueOf(TransactionStatus.Pending));
          paymentRepository.save(payment);
          return "Payment Initial successfully created";
     }

     @Override
     @Transactional
     public PaymentResponse createAndSaveVirtualAccount(String bookingCode, String bank){
          String jsonResponse = createVirtualAccountCode(bookingCode, bank);
          if (jsonResponse == null || jsonResponse.isEmpty()) {
               throw new RuntimeException("Error: No response received from payment service");
          }

          try {
               PaymentResponse paymentResponse = objectMapper.readValue(jsonResponse, PaymentResponse.class);
               PaymentInitial paymentInitial = new PaymentInitial();
               paymentInitial.setBookingCode(bookingCode);
               paymentInitial.setBank(bank);
               paymentInitial.setVaNumber(paymentResponse.getVa_numbers().getFirst().getVa_number());
               createPaymentInitial(paymentInitial);
               return paymentResponse;
          } catch (JsonMappingException e) {
               throw new RuntimeException(e);
          } catch (JsonProcessingException e) {
               throw new RuntimeException(e);
          }
     }

     public Payment getPaymentByBookingCode(String bookingCode) {
        Payment payment =   paymentRepository.findByBookingCode(bookingCode);
        if (payment == null) {
             throw new DataNotFoundException("Payment with booking code "+ bookingCode + " not found");
        }
        return payment;
     }

}
