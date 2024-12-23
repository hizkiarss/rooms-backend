package com.rooms.rooms.peakSeason.entity;

import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.rooms.entity.Rooms;
import com.rooms.rooms.users.entity.Users;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
@Table(name = "peak_season")
@SQLRestriction("deleted_at IS NULL")

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
     private LocalDate startDate;

     @Column(name = "end_date", nullable = false)
     private LocalDate endDate;

     @ColumnDefault("CURRENT_TIMESTAMP")
     @Column(name = "created_at")
     private Instant createdAt = Instant.now();

     @ColumnDefault("CURRENT_TIMESTAMP")
     @Column(name = "updated_at")
     private Instant updatedAt = Instant.now();

     @Column(name = "deleted_at")
     private Instant deletedAt;

     @Column(name = "name", length = Integer.MAX_VALUE)
     private String name;

    @Column(name = "mark_up_value", nullable = false)
    private Double markUpValue;

    @Column(name = "markup_type", length = Integer.MAX_VALUE)
    private String markUpType;

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
