package com.rooms.rooms.payment.entity;

import lombok.Data;

@Data
public class PaymentInitial {
     private String bookingCode;
     private String transactionStatus;
     private String bank;
     private String vaNumber;
     private Double grossAmount;

     public Payment toPayment() {
          Payment payment = new Payment();
          payment.setBookingCode(this.bookingCode);
          payment.setTransactionStatus(this.transactionStatus);
          payment.setBank(this.bank);
          payment.setVaNumber(this.vaNumber);
          payment.setGrossAmount(this.grossAmount);
          return payment;
     }
}
