package com.rooms.rooms.transaction.repository;

import com.rooms.rooms.transaction.entity.Transaction;
import com.rooms.rooms.transaction.entity.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long > {
     List<Transaction> findAllByStatusAndDeletedAtIsNull(TransactionStatus status);
     List<Transaction> findAllByUsersIdAndDeletedAtIsNull(Long id);
     List<Transaction> findAllByPropertiesIdAndDeletedAtIsNull(Long id);
     Transaction findByIdAndDeletedAtIsNull(Long id);
     List<Transaction> findAllByDeletedAtIsNull();
}
