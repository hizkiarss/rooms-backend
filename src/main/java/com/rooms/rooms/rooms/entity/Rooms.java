package com.rooms.rooms.rooms.entity;

import com.rooms.rooms.bedTypes.entity.BedTypes;
import com.rooms.rooms.Booking.Entity.Booking;
import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.roomPicture.entity.RoomPicture;
import com.rooms.rooms.transactionDetail.entity.TransactionDetail;
import jakarta.annotation.Nullable;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "rooms")
@SQLRestriction("deleted_at IS NULL")
public class Rooms implements Serializable {

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

    @Nullable
    @Column(name = "is_available")
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

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
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

    @OneToMany(mappedBy = "room")
    private Set<Booking> bookings = new LinkedHashSet<>();

    @OneToMany(mappedBy = "rooms")
    private List<RoomPicture> roomPictures;

    @OneToMany(mappedBy = "rooms")
    private Set<TransactionDetail> transactionDetails = new LinkedHashSet<>();

    @Column(name = "slug", length = Integer.MAX_VALUE)
    private String slug;

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
