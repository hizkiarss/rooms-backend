package com.rooms.rooms.paymentProof.service.impl;

import com.rooms.rooms.paymentProof.entity.PaymentProof;
import com.rooms.rooms.paymentProof.repository.PaymentProofRepository;
import com.rooms.rooms.paymentProof.service.PaymentProofService;
import com.rooms.rooms.transaction.entity.Transaction;
import com.rooms.rooms.transaction.entity.TransactionStatus;
import com.rooms.rooms.transaction.service.TransactionService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PaymentProofServiceImpl implements PaymentProofService {
     private PaymentProofRepository paymentProofRepository;
     private TransactionService transactionService;
     public PaymentProofServiceImpl(PaymentProofRepository paymentProofRepository, TransactionService transactionService) {
          this.paymentProofRepository = paymentProofRepository;
          this.transactionService = transactionService;
     }

     @Override
     public List<PaymentProof> getPendingManualTransferProofs(){
          return paymentProofRepository.findAllPendingManualTransferProofs();
     }

     @Override
     public String acceptPaymentProof(Long  transactionId){
         return transactionService.updateTransactionStatus(transactionId, TransactionStatus.Success);
     }

     @Override
     public String rejectPaymentProof(Long  transactionId){
          return transactionService.updateTransactionStatus(transactionId, TransactionStatus.Rejected);
     }

     @Override
     public List<PaymentProof> getPendingTransactionProofsByPropertyId(Long propertyId){
     return  paymentProofRepository.findAllPendingTransferProofByPropertyId(propertyId);
     }

     @Override
     public String addPaymentProof(Long transactionId, String imgUrl){
          Transaction transaction = transactionService.getTransactionById(transactionId);
          PaymentProof paymentProof = new PaymentProof();
          paymentProof.setTransaction(transaction);
          paymentProof.setImgUrl(imgUrl);
          paymentProofRepository.save(paymentProof);
          return "PaymentProof successfully added";
     }


}
