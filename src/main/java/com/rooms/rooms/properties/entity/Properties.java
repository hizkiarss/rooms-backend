package com.rooms.rooms.properties.entity;

import com.rooms.rooms.city.entity.City;
import com.rooms.rooms.Booking.Entity.Booking;
import com.rooms.rooms.peakSeason.entity.PeakSeason;
import com.rooms.rooms.propertyCategories.entity.PropertyCategories;
import com.rooms.rooms.propertyFacility.entity.PropertyFacility;
import com.rooms.rooms.propertyPicture.entity.PropertyPicture;
import com.rooms.rooms.review.entity.Review;
import com.rooms.rooms.rooms.entity.Rooms;
import com.rooms.rooms.transaction.entity.Transaction;
import com.rooms.rooms.userFavorite.entity.UsersFavorite;
import com.rooms.rooms.users.entity.Users;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "properties")
@SQLRestriction("deleted_at IS NULL")
public class Properties implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "property_id_gen")
    @SequenceGenerator(name = "property_id_gen", sequenceName = "property_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id")
    private Users users;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "property_category_id")
    private PropertyCategories propertyCategories;

    @Column(name = "description")
    private String description;

    @Column(name = "check_in_time", nullable = false)
    private LocalTime checkInTime;

    @Column(name = "check_out_time", nullable = false)
    private LocalTime checkOutTime;

    @Column(name = "address", nullable = false)
    private String address;

    @OneToMany(mappedBy = "properties", fetch = FetchType.EAGER)
    private List<Review> reviews;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "city")
    private City city;

    @OneToMany(mappedBy = "properties", fetch = FetchType.EAGER)
    private List<PeakSeason> peakSeasons;

    @OneToMany(mappedBy = "property", fetch = FetchType.EAGER)
    private List<PropertyFacility> propertyFacilities;

    @OneToMany(mappedBy = "properties", fetch = FetchType.EAGER)
    private List<PropertyPicture> propertyPictures;

    @OneToMany(mappedBy = "properties", fetch = FetchType.EAGER)
    private List<Rooms> rooms;

    @OneToMany(mappedBy = "properties", fetch = FetchType.EAGER)
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "properties", fetch = FetchType.EAGER)
    private List<UsersFavorite> userFavorites;

    @OneToMany(mappedBy = "property")
    private Set<Booking> bookings = new LinkedHashSet<>();

    @Column(name = "total_review", precision = 2, scale = 1)
    private Integer totalReview;

    @Column(name = "average_rating")
    private Double averageRating;

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
