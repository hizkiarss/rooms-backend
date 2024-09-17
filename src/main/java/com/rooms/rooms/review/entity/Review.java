package com.rooms.rooms.review.entity;

import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.transaction.entity.Transaction;
import com.rooms.rooms.users.entity.Users;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import java.io.Serializable;


import java.time.Instant;

@Data
@Entity
@Table(name = "review")
public class Review implements Serializable{

     @Id
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_id_gen")
     @SequenceGenerator(name = "review_id_gen", sequenceName = "review_id_seq", allocationSize = 1)
     @Column(name = "id", nullable = false)
     private Long id;

     @Column(name = "feedback", nullable = false)
     private String feedback;

     @Column(name = "rating", nullable = false)
     private Integer rating;

     @Column(name = "reply")
     private String reply;

     @ManyToOne(fetch = FetchType.LAZY, optional = false)
     @JoinColumn(name = "transaction_id")
     private Transaction transaction;

     @ManyToOne(fetch = FetchType.LAZY, optional = false)
     @JoinColumn(name = "user_id")
     private Users users;

     @ManyToOne(fetch = FetchType.LAZY, optional = false)
     @JoinColumn(name = "property_id")
     private Properties properties;

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
