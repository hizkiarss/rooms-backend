package com.rooms.rooms.auth.entity;

import com.rooms.rooms.users.entity.Users;
import jakarta.persistence.*;
import java.time.LocalDateTime;

    @Entity
    @Table(name = "secureTokens")
    public class SecureToken {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(unique = true)
        private String token;

        @Column(updatable = false)
        @Basic(optional = false)
        private LocalDateTime expireAt;

        @ManyToOne
        @JoinColumn(name = "users_id", referencedColumnName = "id")
        private Users user;

    }
