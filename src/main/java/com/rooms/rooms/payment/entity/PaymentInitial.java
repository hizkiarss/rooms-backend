package com.rooms.rooms.payment.entity;

import lombok.Data;

@Data
public class PaymentInitial {
     private String bookingCode;
     private String bank;
     private String vaNumber;

     public Payment toPayment() {
          Payment payment = new Payment();
          payment.setBookingCode(this.bookingCode);
          payment.setBank(this.bank);
          payment.setVaNumber(this.vaNumber);
          return payment;
     }
}
