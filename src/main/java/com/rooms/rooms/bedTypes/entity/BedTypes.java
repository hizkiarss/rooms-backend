package com.rooms.rooms.bedTypes.entity;

import jakarta.annotation.PreDestroy;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Data
@Entity
@Table(name = "bed_types")
public class BedTypes {

     @Id
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bed_types_id_gen")
     @SequenceGenerator(name = "bed_types_id_gen", sequenceName = "bed_types_id_seq", allocationSize = 1)
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