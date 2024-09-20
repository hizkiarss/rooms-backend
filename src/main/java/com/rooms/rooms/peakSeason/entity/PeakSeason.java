package com.rooms.rooms.peakSeason.entity;

import com.rooms.rooms.properties.entity.Properties;
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
@Table(name = "peak_season")
public class PeakSeason implements Serializable {

     @Id
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "peak_season_id_gen")
     @SequenceGenerator(name = "peak_season_id_gen", sequenceName = "peak_season_id_seq", allocationSize = 1)
     @Column(name = "id", nullable = false)
     private Long id;

     @ManyToOne(fetch = FetchType.LAZY, optional = false)
     @JoinColumn(name = "property_id")
     private Properties properties;

     @Column(name = "start_date", nullable = false)
     private Date startDate;

     @Column(name = "end_date", nullable = false)
     private Date end_date;

     @Column(name = "mark_up_percentage", nullable = false)
     private Double markUpPercentage;

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
