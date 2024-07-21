package com.quest.etna.model;

import java.sql.Date;

import org.springframework.format.annotation.DateTimeFormat;

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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

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

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "creationAt", nullable = false)
    private Date creationAt;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "UpdateAt", nullable = false)
    private Date updateAt;

    // Constructors
    public User() {
    }

    /**
     * [User role]
     * Default set to ROLE_USER
     */
    @PrePersist
    public void onCreate() {
        if (role == null) {
            role = UserRole.ROLE_USER;
        }
        if (creationAt == null) {
            creationAt = new Date(System.currentTimeMillis());
        }
    }

    // Getters

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }

    public Date getCreationAt() {
        return creationAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void setCreationAt(Date creationAt) {
        this.creationAt = creationAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    /**
     * [User equals]
     * Real comparison between two objects (User) not just their references
     * 
     * @param obj User
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;

        User user = (User) obj;

        return user.getId() == this.getId();
    }

    /**
     * [User hashCode]
     * Generate a hash code for the User object based on its id
     * (used for speeding up comparison)
     * 
     * @return int based on the id
     */
    @Override
    public int hashCode() {
        return Long.hashCode(this.getId());
    }

    /**
     * [User toString]
     * Convert the User object to a string
     * 
     * @return String
     */
    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", role=" + role
                + ", creationAt=" + creationAt + ", updateAt=" + updateAt + "]";
    }

}