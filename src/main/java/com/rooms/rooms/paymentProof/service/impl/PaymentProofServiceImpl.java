package com.rooms.rooms.paymentProof.service.impl;

import com.rooms.rooms.Booking.Entity.Booking;
import com.rooms.rooms.Booking.Service.BookingService;
import com.rooms.rooms.email.EmailService;
import com.rooms.rooms.paymentProof.entity.PaymentProof;
import com.rooms.rooms.paymentProof.repository.PaymentProofRepository;
import com.rooms.rooms.paymentProof.service.PaymentProofService;
import com.rooms.rooms.transaction.entity.Transaction;
import com.rooms.rooms.transaction.entity.TransactionStatus;
import com.rooms.rooms.transaction.service.TransactionService;
import com.rooms.rooms.transactionDetail.entity.TransactionDetail;
import com.rooms.rooms.transactionDetail.service.TransactionDetailService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class PaymentProofServiceImpl implements PaymentProofService {
     private PaymentProofRepository paymentProofRepository;
     private TransactionService transactionService;
     private EmailService emailService;
     private TransactionDetailService transactionDetailService;
     private BookingService bookingService;

     public PaymentProofServiceImpl(PaymentProofRepository paymentProofRepository, TransactionService transactionService, EmailService emailService, TransactionDetailService transactionDetailService, BookingService bookingService) {
          this.paymentProofRepository = paymentProofRepository;
          this.transactionService = transactionService;
          this.emailService = emailService;
          this.transactionDetailService = transactionDetailService;
          this.bookingService = bookingService;
     }

     @Override
     public List<PaymentProof> getPendingManualTransferProofs() {
          return paymentProofRepository.findAllPendingManualTransferProofs();
     }

     @Override
     @Transactional
     public String acceptPaymentProof(Long transactionId) {
          Transaction transaction = transactionService.getTransactionById(transactionId);
          LocalDate startDate = transaction.getTransactionDetails().get(0).getStartDate();
          LocalDate endDate = transaction.getTransactionDetails().get(0).getEndDate();
          int daysBetween = (int) ChronoUnit.DAYS.between(startDate, endDate);
          String roomName = transaction.getTransactionDetails().get(0).getRooms().getName();
          Integer adult = transaction.getAdult();
          Integer children = transaction.getChildren();
          String name = transaction.getUsers().getUsername();
          String email = transaction.getUsers().getEmail();
          String firstName = transaction.getFirstName();
          String lastName = transaction.getLastName();
          String bookingCode = transaction.getBookingCode();
          String htmlBody = emailService.getConfirmationEmailTemplate(email, name, bookingCode, transaction.getProperties(), firstName, lastName, adult, children, daysBetween, roomName);
          emailService.sendEmail(email, "Booking Success! Your Stay is Officially Reserved", htmlBody);
          return transactionService.updateTransactionStatus(transactionId, TransactionStatus.Success);
     }

     public PaymentProof getPaymentProofById(Long paymentProofId) {
          return paymentProofRepository.findById(paymentProofId).get();
     }

     @Override
     @Transactional
     public String rejectPaymentProof(Long transactionId) {
          Transaction transaction = transactionService.getTransactionById(transactionId);
          TransactionDetail transactionDetail = transactionDetailService.getTransactionDetailByTransactionId(transaction.getId());
          Booking booking = bookingService.getBookingByTransactionDetailId(transactionDetail.getId());
          PaymentProof paymentProof = getPaymentProofById(transaction.getPaymentProofs().get(0).getId());
//          bookingService.deleteBookingById(booking.getId());
//          transactionDetailService.deleteTransactionDetailById(transactionDetail.getId());
          paymentProofRepository.delete(paymentProof);

          return transactionService.updateTransactionStatus(transactionId, TransactionStatus.Pending);
     }

     @Override
     public List<PaymentProof> getPendingTransactionProofsByPropertyId(Long propertyId) {
          return paymentProofRepository.findAllPendingTransferProofByPropertyId(propertyId);
     }

     @Override
     public List<PaymentProof> getCheckTransactionProofsByPropertyId(Long propertyId) {
          return paymentProofRepository.findAllCheckTransferProofByPropertyId(propertyId);
     }

     @Override
     @Transactional
     public String addPaymentProof(Long transactionId, String imgUrl) {
          Transaction transaction = transactionService.getTransactionById(transactionId);
          PaymentProof paymentProof = new PaymentProof();
          paymentProof.setTransaction(transaction);
          paymentProof.setImgUrl(imgUrl);
          paymentProofRepository.save(paymentProof);
          transactionService.updateTransactionStatus(transaction.getId(), TransactionStatus.Check);
          return "PaymentProof successfully added";
     }


}
