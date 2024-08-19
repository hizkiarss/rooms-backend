package com.rooms.rooms.transactionDetail.entity;

import com.rooms.rooms.rooms.entity.Rooms;
import com.rooms.rooms.transaction.entity.Transaction;
import com.rooms.rooms.users.entity.Users;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.Date;

@Data
@Entity
@Table(name = "transaction_detail")
public class TransactionDetail {

     @Id
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_detail_id_gen")
     @SequenceGenerator(name = "transaction_detail_id_gen", sequenceName = "transaction_detail_id_seq", allocationSize = 1)
     @Column(name = "id", nullable = false)
     private Long id;

     @ManyToOne(fetch = FetchType.LAZY, optional = false)
     @JoinColumn(name = "rooms_id")
     private Rooms rooms;

     @ManyToOne(fetch = FetchType.LAZY, optional = false)
     @JoinColumn(name = "transaction_id")
     private Transaction transaction;

     @Column(name = "price", nullable = false)
     private Double price;

     @Column(name = "start_date", nullable = false)
     private Date startDate;

     @Column(name = "end_date", nullable = false)
     private Date end_date;

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
