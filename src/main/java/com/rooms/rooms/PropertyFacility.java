package com.rooms.rooms;

import com.rooms.rooms.facilities.entity.Facilities;
import com.rooms.rooms.properties.entity.Properties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "property_facility")
public class PropertyFacility {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "property_facility_id_gen")
    @SequenceGenerator(name = "property_facility_id_gen", sequenceName = "property_facility_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "property_id", nullable = false)
    private Properties property;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "facilities_id", nullable = false)
    private Facilities facilities;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "deleted_at")
    private Instant deletedAt;

}