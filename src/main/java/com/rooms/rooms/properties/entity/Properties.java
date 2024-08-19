package com.rooms.rooms.properties.entity;

import com.rooms.rooms.propertyCategories.entity.PropertyCategories;
import com.rooms.rooms.users.entity.Users;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

@Data
@Entity
@Table(name = "properties")
public class Properties  implements Serializable {

     @Id
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "property_id_gen")
     @SequenceGenerator(name = "property_id_gen", sequenceName = "property_id_seq", allocationSize = 1)
     @Column(name = "id", nullable = false)
     private Long id;

     @ManyToOne(fetch = FetchType.LAZY, optional = false)
     @JoinColumn(name = "user_id")
     private Users users;

     @Column(name = "name", nullable = false)
     private String name;

     @ManyToOne(fetch = FetchType.LAZY, optional = false)
     @JoinColumn(name = "property_category_id")
     private PropertyCategories propertyCategories;

     @Column(name = "description")
     private String description;

     @Column(name = "check_in_time", nullable = false)
     private Date checkInTime;

     @Column(name = "check_out_time", nullable = false)
     private Date checkOutTime;

     @Column(name = "address", nullable = false)
     private String address;

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
