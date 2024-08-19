package com.rooms.rooms.transaction.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "transaction")
public class Transaction {
     @Id
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_id_gen")
     @SequenceGenerator(name = "transaction_id_gen", sequenceName = "transaction_id_seq", allocationSize = 1)
     @Column(name = "id", nullable = false)
     private Long id;

     @Column(name = "name", nullable = false)
     private String name;

}
