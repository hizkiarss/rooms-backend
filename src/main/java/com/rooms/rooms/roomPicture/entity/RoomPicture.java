package com.rooms.rooms.roomPicture.entity;

import com.rooms.rooms.propertyCategories.entity.PropertyCategories;
import com.rooms.rooms.rooms.entity.Rooms;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLRestriction;

import java.time.Instant;

@Data
@Entity
@Table(name = "room_picture")
@SQLRestriction("deleted_at IS NULL")
public class RoomPicture {

     @Id
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "room_picture_id_gen")
     @SequenceGenerator(name = "room_picture_id_gen", sequenceName = "room_picture_id_seq", allocationSize = 1)
     @Column(name = "id", nullable = false)
     private Long id;

     @ManyToOne(fetch = FetchType.EAGER, optional = false)
     @JoinColumn(name = "room_id")
     private Rooms rooms;

     @Column(name = "img_url", nullable = false)
     private String imgUrl;

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
