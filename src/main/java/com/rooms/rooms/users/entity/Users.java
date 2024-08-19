package com.rooms.rooms.users.entity;

import com.rooms.rooms.role.entity.Role;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.time.Instant;

@Data
@Entity
@Table(name = "users")
public class Users implements Serializable {

     @Id
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_gen")
     @SequenceGenerator(name = "users_id_gen", sequenceName = "users_id_seq", allocationSize = 1)
     @Column(name = "id", nullable = false)
     private Long id;

     @Column(name = "email", nullable = false)
     private String email;

     @Column(name = "username", nullable = false)
     private String username;

     @Column(name = "password", nullable = false)
     private String password;

     @Column(name = "profile_picture")
     private String profilePicture;

     @ManyToOne(fetch = FetchType.LAZY, optional = false)
     @JoinColumn(name = "role_id")
     private Role role;

     @Column(name = "mobile_number")
     private String mobileNumber;

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
