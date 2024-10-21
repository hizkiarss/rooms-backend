package com.rooms.rooms.transaction.repository;

import com.rooms.rooms.transaction.entity.Transaction;
import com.rooms.rooms.transaction.entity.TransactionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
     List<Transaction> findAllByStatusAndDeletedAtIsNull(TransactionStatus status);

     Page<Transaction> findAllByUsersIdAndDeletedAtIsNull(Long id, Pageable pageable);

     Page<Transaction> findAllByUsersIdAndStatusAndDeletedAtIsNull(Long usersId, TransactionStatus status, Pageable pageable);

     List<Transaction> findAllByPropertiesIdAndDeletedAtIsNull(Long id);

     List<Transaction> findAllByPropertiesIdAndDeletedAtIsNullAndCreatedAtBetween(Long propertyId, Instant startDate, Instant endDate);

     Transaction findByIdAndDeletedAtIsNull(Long id);

     List<Transaction> findAllByDeletedAtIsNull();

     Transaction findByBookingCodeAndDeletedAtIsNull(String bookingCode);

     @Query("SELECT COALESCE(SUM(t.finalPrice - t.tax), 0) FROM Transaction t " +
             "WHERE t.properties.id = :propertyId " +
             "AND t.status = com.rooms.rooms.transaction.entity.TransactionStatus.Success " +
             "AND t.createdAt >= :startDate AND t.createdAt <= :endDate")
     BigDecimal getTotalRevenueByPropertyId(@Param("propertyId") Long propertyId,
                                            @Param("startDate") Instant startDate,
                                            @Param("endDate") Instant endDate);

     @Query("SELECT COALESCE(SUM(t.tax), 0) FROM Transaction t WHERE t.properties.id = :propertyId " +
             "AND t.status = com.rooms.rooms.transaction.entity.TransactionStatus.Success AND t.createdAt >= :startDate AND t.createdAt <= :endDate")
     BigDecimal getTotalTaxByPropertyId(@Param("propertyId") Long propertyId,
                                        @Param("startDate") Instant startDate,
                                        @Param("endDate") Instant endDate);

     @Query("SELECT COALESCE(SUM(t.finalPrice), 0) FROM Transaction t WHERE t.properties.id = :propertyId " +
             "AND t.status = com.rooms.rooms.transaction.entity.TransactionStatus.Success AND t.createdAt >= :startDate AND t.createdAt <= :endDate")
     BigDecimal getTotalRevenueWithTaxByPropertyId(@Param("propertyId") Long propertyId,
                                                   @Param("startDate") Instant startDate,
                                                   @Param("endDate") Instant endDate);

     @Query("SELECT COUNT(t) FROM Transaction t WHERE t.properties.id = :propertyId " +
             "AND t.status = com.rooms.rooms.transaction.entity.TransactionStatus.Success " +
             "AND t.createdAt >= :startDate AND t.createdAt <= :endDate")
     Integer countTotalTransactionsByPropertyId(@Param("propertyId") Long propertyId,
                                                @Param("startDate") Instant startDate,
                                                @Param("endDate") Instant endDate);

     List<Transaction> findTop5ByStatusAndPropertiesIdAndDeletedAtIsNullOrderByCreatedAtDesc(TransactionStatus status, Long propertyId);

}
