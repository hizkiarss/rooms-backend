package com.rooms.rooms.rooms.entity;

import com.rooms.rooms.bedTypes.entity.BedTypes;
import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.propertyCategories.entity.PropertyCategories;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Data
@Entity
@Table(name = "rooms")
public class Rooms {

     @Id
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rooms_id_gen")
     @SequenceGenerator(name = "rooms_id_gen", sequenceName = "rooms_id_seq", allocationSize = 1)
     @Column(name = "id", nullable = false)
     private Long id;

     @Column(name = "name", nullable = false)
     private String name;

     @Column(name = "description")
     private String description;

     @Column(name = "capacity")
     private Integer capacity;

     @Column(name = "is_booked", nullable = false)
     private Boolean isBooked;

     @Column(name = "is_available", nullable = false)
     private Boolean isAvailable;

     @Column(name = "room_number", nullable = false)
     private String roomNumber;

     @Column(name = "price", nullable = false)
     private Double price;

     @ManyToOne(fetch = FetchType.LAZY, optional = false)
     @JoinColumn(name = "property_id")
     private Properties properties;

     @Column(name = "include_breakfast", nullable = false)
     private Boolean includeBreakfast;

     @ManyToOne(fetch = FetchType.LAZY, optional = false)
     @JoinColumn(name = "bed_type_id")
     private BedTypes bedTypes;

     @Column(name = "room_area", nullable = false)
     private Integer roomArea;

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
