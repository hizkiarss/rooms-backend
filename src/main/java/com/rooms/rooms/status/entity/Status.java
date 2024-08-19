package com.rooms.rooms.status.entity;

import jakarta.annotation.PreDestroy;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Data
@Entity
@Table(name = "status")
public class Status {
     @Id
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "status_id_gen")
     @SequenceGenerator(name = "status_id_gen", sequenceName = "status_id_seq", allocationSize = 1)
     @Column(name = "id", nullable = false)
     private Long id;

     @Column(name = "name", nullable = false)
     private String name;

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
