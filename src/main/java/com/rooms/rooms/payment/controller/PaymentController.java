package com.rooms.rooms.payment.controller;

import com.rooms.rooms.payment.entity.PaymentRequest;
import com.rooms.rooms.payment.service.PaymentService;
import com.rooms.rooms.transaction.service.TransactionService;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/payments")
@Log
public class PaymentController {

     private PaymentService paymentService;
     private TransactionService transactionService;
     public PaymentController(PaymentService paymentService, TransactionService transactionService) {
          this.paymentService = paymentService;
          this.transactionService = transactionService;
     }

     @PostMapping("/create")
     public String createPayment(@RequestBody PaymentRequest paymentRequest) {
     return paymentService.createTransaction(paymentRequest);

     }

     @PostMapping("/notification")
     public ResponseEntity<String> handlePaymentNotification(@RequestBody Map<String, Object> payload) {
          String transactionStatus = (String) payload.get("transaction_status");
          String orderId = (String) payload.get("order_id");
          String signature = (String) payload.get("signature_key");

          if ("capture".equals(transactionStatus) || "settlement".equals(transactionStatus)) {
               transactionService.acceptTransaction(orderId, signature);
          } else if ("pending".equals(transactionStatus)) {
              transactionService.pendingTransaction(orderId);
          } else if ("expire".equals(transactionStatus) ) {
               transactionService.expireTransaction(orderId);
          }else if ("cancel".equals(transactionStatus) ) {
               transactionService.cancelTransaction(orderId);
          }
          return ResponseEntity.ok("Notification received successfully");
     }
}


