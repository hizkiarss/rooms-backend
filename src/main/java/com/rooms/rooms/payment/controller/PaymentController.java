//package com.rooms.rooms.payment.controller;
//
//import com.rooms.rooms.payment.entity.PaymentRequest;
//import com.rooms.rooms.payment.service.PaymentService;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/payments")
//public class PaymentController {
//
//     private PaymentService paymentService;
//     public PaymentController(PaymentService paymentService) {
//          this.paymentService = paymentService;
//     }
//
//     @PostMapping("/create")
//     public String createPayment(@RequestBody PaymentRequest paymentRequest) {
//     return paymentService.createTransaction(paymentRequest);
//
//     }
//}
//
//
