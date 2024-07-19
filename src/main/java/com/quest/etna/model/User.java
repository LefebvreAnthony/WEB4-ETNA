package com.quest.etna.model;

import com.quest.etna.enums.UserRole;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

@Entity
public class User {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Basic
    @Column(name = "username", nullable = false, length = 255)
    private String username;

    @Basic
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Basic
    @Column(name = "role", nullable = false, length = 255)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    /**
     * [User role]
     * Default set to ROLE_USER
     */
    @PrePersist
    public void onCreate() {
        if (role == null) {
            role = UserRole.ROLE_USER;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}