package com.rooms.rooms.paymentProof.entity;

import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.transaction.entity.Transaction;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Data
@Entity
@Table(name = "payment_proof")
public class PaymentProof {

     @Id
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_proof_id_gen")
     @SequenceGenerator(name = "payment_proof_id_gen", sequenceName = "payment_proof_id_seq", allocationSize = 1)
     @Column(name = "id", nullable = false)
     private Long id;

     @Column(name = "img_url", nullable = false)
     private String imgUrl;

     @ManyToOne(fetch = FetchType.LAZY, optional = false)
     @JoinColumn(name = "transaction_id")
     private Transaction transaction;

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
