package com.rooms.rooms.payment.service;

import com.rooms.rooms.payment.entity.Payment;
import com.rooms.rooms.payment.entity.PaymentInitial;
import com.rooms.rooms.payment.entity.PaymentRequest;
import com.rooms.rooms.payment.entity.PaymentResponse;

public interface PaymentService {
      String createTransaction(PaymentRequest paymentRequest) ;
      String createVirtualAccountCode(String bookingCode, String bank);
      PaymentResponse createAndSaveVirtualAccount(String bookingCode, String bank);
      String createPaymentInitial(PaymentInitial paymentInitial);
      Payment getPaymentByBookingCode(String bookingCode);
}
