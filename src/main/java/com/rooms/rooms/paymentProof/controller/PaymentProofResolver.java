package com.rooms.rooms.paymentProof.controller;

import com.rooms.rooms.paymentProof.entity.PaymentProof;
import com.rooms.rooms.paymentProof.service.PaymentProofService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class PaymentProofResolver {
     private PaymentProofService paymentProofService;
     public PaymentProofResolver(PaymentProofService paymentProofService) {
          this.paymentProofService = paymentProofService;
     }

     @QueryMapping(value = "pendingPaymentProof")
     public List<PaymentProof> getPendingPaymentProofs() {
          return paymentProofService.getPendingManualTransferProofs();
     }

     @QueryMapping(value = "pendingPaymentProofByPropertyId")
     public List<PaymentProof> getPendingPaymentProofs(@Argument Long propertyId) {
          return paymentProofService.getPendingTransactionProofsByPropertyId(propertyId);
     }

     @MutationMapping(value = "acceptPaymentProof")
     public String acceptPaymentProof(@Argument Long transactionId) {
          return paymentProofService.acceptPaymentProof(transactionId);
     }

     @MutationMapping(value = "rejectPaymentProof")
     public String rejectPaymentProof(@Argument Long transactionId) {
          return paymentProofService.rejectPaymentProof(transactionId);
     }

     @MutationMapping(value = "addPaymentProof")
     public String addPaymentProof(@Argument Long transactionId, @Argument String imgUrl) {
          return paymentProofService.addPaymentProof(transactionId, imgUrl);
     }
}
