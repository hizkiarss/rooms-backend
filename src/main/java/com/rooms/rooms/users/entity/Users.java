package com.rooms.rooms.users.entity;

import com.rooms.rooms.auth.entity.RoleName;

import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.review.entity.Review;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@SQLRestriction("deleted_at IS NULL")
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

     @Column(name = "password", nullable = true)
     private String password;

     @Column(name = "profile_picture", nullable = true)
     private String profilePicture;

     @Enumerated(EnumType.STRING)
     @Column(name = "role", nullable = false)
     private RoleName role;

     @Column(name = "mobile_number")
     private String mobileNumber;

     @OneToMany(mappedBy = "users", fetch = FetchType.EAGER)
     private List<Properties> properties;

     @ColumnDefault("CURRENT_TIMESTAMP")
     @Column(name = "created_at")
     private Instant createdAt = Instant.now();

     @ColumnDefault("CURRENT_TIMESTAMP")
     @Column(name = "updated_at")
     private Instant updatedAt = Instant.now();

     @Column(name = "deleted_at")
     private Instant deletedAt;

     @Column(name = "is_verified")
     private Boolean isVerified;

     @Enumerated(EnumType.STRING)
     @Column(name = "gender", length = Integer.MAX_VALUE)
     private Gender gender;

     @Column(name = "date_of_birth")
     private LocalDate dateOfBirth;

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
