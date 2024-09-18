package com.rooms.rooms.paymentProof.service.impl;

import com.rooms.rooms.email.EmailService;
import com.rooms.rooms.paymentProof.entity.PaymentProof;
import com.rooms.rooms.paymentProof.repository.PaymentProofRepository;
import com.rooms.rooms.paymentProof.service.PaymentProofService;
import com.rooms.rooms.transaction.entity.Transaction;
import com.rooms.rooms.transaction.entity.TransactionStatus;
import com.rooms.rooms.transaction.service.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PaymentProofServiceImpl implements PaymentProofService {
     private PaymentProofRepository paymentProofRepository;
     private TransactionService transactionService;
     private EmailService emailService;
     public PaymentProofServiceImpl(PaymentProofRepository paymentProofRepository, TransactionService transactionService, EmailService emailService) {
          this.paymentProofRepository = paymentProofRepository;
          this.transactionService = transactionService;
          this.emailService = emailService;
     }

     @Override
     public List<PaymentProof> getPendingManualTransferProofs(){
          return paymentProofRepository.findAllPendingManualTransferProofs();
     }

     @Override
     @Transactional
     public String acceptPaymentProof(Long  transactionId){
          Transaction transaction = transactionService.getTransactionById(transactionId);
          String htmlBody = emailService.getConfirmationEmailTemplate("kmr.oblay96@gmail.com", transaction.getUsers().getUsername(), transaction.getBookingCode(), transaction.getProperties(), transaction.getFirstName(), transaction.getLastName() );
          emailService.sendEmail("kmr.oblay96@gmail.com", "Reservation details", htmlBody);
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
