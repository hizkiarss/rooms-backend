package com.rooms.rooms.payment.entity;

import jakarta.annotation.PreDestroy;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Data
@Entity
@Table(name = "payment")
public class Payment {
     @Id
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_id_gen")
     @SequenceGenerator(name = "payment_id_gen", sequenceName = "payment_id_seq", allocationSize = 1)
     @Column(name = "id", nullable = false)
     private Long id;

     @Column(name = "booking_code", nullable = false)
     private String bookingCode;

     @Column(name = "transaction_status", nullable = false)
     private String transactionStatus;

     @Column(name = "bank", nullable = false)
     private String bank;

     @Column(name = "va_number", nullable = false)
     private String vaNumber;

     @Column(name = "gross_amount", nullable = false)
     private Double grossAmount;

     @ColumnDefault("CURRENT_TIMESTAMP")
     @Column(name = "created_at")
     private Instant createdAt = Instant.now();

     @ColumnDefault("CURRENT_TIMESTAMP")
     @Column(name = "updated_at")
     private Instant updatedAt = Instant.now();

     @Column(name = "deleted_at")
     private Instant deletedAt;

     @PrePersist
     void onSave() {
          this.createdAt = Instant.now();
          this.updatedAt = Instant.now();
     }

     @PreUpdate
     void onUpdate() {
          this.updatedAt = Instant.now();
     }

     @PreDestroy
     void onDelete() {
          this.deletedAt = Instant.now();
     }
}
