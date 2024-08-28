package com.rooms.rooms.paymentProof.repository;

import com.rooms.rooms.paymentProof.entity.PaymentProof;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentProofRepository extends JpaRepository<PaymentProof, Long> {
     @Query("SELECT pp FROM PaymentProof pp JOIN pp.transaction t WHERE t.status = com.rooms.rooms.transaction.entity.TransactionStatus.Pending AND t.paymentMethod = com.rooms.rooms.transaction.entity.TransactionPaymentMethod.manual_transfer")
     List<PaymentProof> findAllPendingManualTransferProofs();

     @Query("SELECT pp FROM PaymentProof pp JOIN pp.transaction t WHERE t.status = com.rooms.rooms.transaction.entity.TransactionStatus.Pending AND t.paymentMethod = com.rooms.rooms.transaction.entity.TransactionPaymentMethod.manual_transfer AND t.properties.id = :propertyId")
     List<PaymentProof> findAllPendingTransferProofByPropertyId(@Param("propertyId") Long propertyId);

}
