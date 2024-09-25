package com.rooms.rooms.transactionDetail.repository;

import com.rooms.rooms.transactionDetail.entity.TransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionDetailRepository extends JpaRepository<TransactionDetail, Long> {
     TransactionDetail findByTransactionIdAndDeletedAtIsNull(Long transactionId);
     TransactionDetail findByIdAndDeletedAtIsNull(Long id);
}
