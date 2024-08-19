package com.rooms.rooms.transaction.repository;

import com.rooms.rooms.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long > {
     List<Transaction> findAllByStatusIdAndDeletedAtIsNull(Long id);
     List<Transaction> findAllByUsersIdAndDeletedAtIsNull(Long id);
     List<Transaction> findAllByPropertiesIdAndDeletedAtIsNull(Long id);
     Transaction findByIdAndDeletedAtIsNull(Long id);
     List<Transaction> findAllByDeletedAtIsNull();
}
