package com.rooms.rooms.transaction.entity;

import com.rooms.rooms.bedTypes.entity.BedTypes;
import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.transaction.dto.TransactionRequest;
import com.rooms.rooms.transaction.dto.TransactionResponse;
import com.rooms.rooms.users.entity.Users;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.Date;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {

     @Id
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transactions_id_gen")
     @SequenceGenerator(name = "transactions_id_gen", sequenceName = "transactions_id_seq", allocationSize = 1)
     @Column(name = "id", nullable = false)
     private Long id;

     @ManyToOne(fetch = FetchType.LAZY, optional = false)
     @JoinColumn(name = "user_id")
     private Users users;

     @ManyToOne(fetch = FetchType.LAZY, optional = false)
     @JoinColumn(name = "property_id")
     private Properties properties;

     @Column(name = "final_price", nullable = false)
     private Double finalPrice;

     @Column(name = "status", nullable = false)
     private String status;

     @Column(name = "payment_method", nullable = false)
     private String paymentMethod;

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

     public TransactionResponse toTransactionResponse(){
         TransactionResponse transactionResponse = new TransactionResponse();
          transactionResponse.setId(this.id);
          transactionResponse.setStatus(this.status);
          transactionResponse.setFinalPrice(this.finalPrice);
          transactionResponse.setPaymentMethod(this.paymentMethod);
          return transactionResponse;
     }
}
