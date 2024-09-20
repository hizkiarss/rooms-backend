package com.rooms.rooms.transaction.repository;

import com.rooms.rooms.transaction.entity.Transaction;
import com.rooms.rooms.transaction.entity.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
     List<Transaction> findAllByStatusAndDeletedAtIsNull(TransactionStatus status);

     List<Transaction> findAllByUsersIdAndDeletedAtIsNull(Long id);

     List<Transaction> findAllByPropertiesIdAndDeletedAtIsNull(Long id);
     List<Transaction> findAllByPropertiesIdAndDeletedAtIsNullAndCreatedAtBetween(Long propertyId, Instant startDate, Instant endDate);

     Transaction findByIdAndDeletedAtIsNull(Long id);

     List<Transaction> findAllByDeletedAtIsNull();

     Transaction findByBookingCodeAndDeletedAtIsNull(String bookingCode);

     @Query("SELECT COALESCE(SUM(t.finalPrice), 0) FROM Transaction t WHERE t.properties.id = :propertyId " +
             "AND t.status = com.rooms.rooms.transaction.entity.TransactionStatus.Success AND t.createdAt >= :startDate AND t.createdAt <= :endDate")
     BigDecimal getTotalRevenueByPropertyId(@Param("propertyId") Long propertyId,
                                            @Param("startDate") Instant startDate,
                                            @Param("endDate") Instant endDate);
}
