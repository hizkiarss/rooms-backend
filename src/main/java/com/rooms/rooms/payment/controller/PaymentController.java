package com.rooms.rooms.payment.controller;

import com.rooms.rooms.payment.entity.PaymentRequest;
import com.rooms.rooms.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
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
          // Log data notifikasi untuk melihat isinya
          System.out.println("Payment notification received: " + payload);

          // Proses payload notifikasi
          String transactionStatus = (String) payload.get("transaction_status");
          String orderId = (String) payload.get("order_id");

          // Lakukan sesuatu dengan status transaksi, misalnya:
          if ("capture".equals(transactionStatus) || "settlement".equals(transactionStatus)) {
               // Pembayaran berhasil, update status transaksi di database
               System.out.println("Pembayaran berhasil untuk order: " + orderId);
          } else if ("pending".equals(transactionStatus)) {
               // Pembayaran masih pending
               System.out.println("Pembayaran pending untuk order: " + orderId);
          } else if ("expire".equals(transactionStatus) || "cancel".equals(transactionStatus)) {
               // Pembayaran gagal
               System.out.println("Pembayaran gagal untuk order: " + orderId);
          }

          return ResponseEntity.ok("Notification received successfully");
     }



}


