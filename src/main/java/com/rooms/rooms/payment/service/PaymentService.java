package com.rooms.rooms.payment.service;

import com.rooms.rooms.payment.entity.PaymentInitial;
import com.rooms.rooms.payment.entity.PaymentRequest;

public interface PaymentService {
      String createTransaction(PaymentRequest paymentRequest) ;
      String createVirtualAccountCode(String bookingCode, String bank);
      String createPaymentInitial(PaymentInitial paymentInitial);
}
