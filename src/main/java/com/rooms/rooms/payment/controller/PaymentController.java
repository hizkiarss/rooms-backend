package com.rooms.rooms.payment.controller;

import com.rooms.rooms.payment.entity.PaymentRequest;
import com.rooms.rooms.payment.service.PaymentService;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@Log
public class PaymentController {

     private PaymentService paymentService;
     public PaymentController(PaymentService paymentService) {
          this.paymentService = paymentService;
     }

     @PostMapping("/create")
     public String createPayment(@RequestBody PaymentRequest paymentRequest) {
     return paymentService.createTransaction(paymentRequest);

     }

     @PostMapping("/notification")
     public ResponseEntity<String> handlePaymentNotification(@RequestBody Map<String, Object> payload) {
          log.info("Payment notification received: " + payload);

          String transactionStatus = (String) payload.get("transaction_status");
          String orderId = (String) payload.get("order_id");

          if ("capture".equals(transactionStatus) || "settlement".equals(transactionStatus)) {
               log.info("Pembayaran berhasil untuk order: " + orderId);
          } else if ("pending".equals(transactionStatus)) {
              log.info("Pembayaran pending untuk order: " + orderId);
          } else if ("expire".equals(transactionStatus) || "cancel".equals(transactionStatus)) {
               System.out.println("Pembayaran gagal untuk order: " + orderId);
          }
          return ResponseEntity.ok("Notification received successfully");
     }
}


