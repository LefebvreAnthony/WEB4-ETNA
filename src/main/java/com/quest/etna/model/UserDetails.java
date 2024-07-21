package com.quest.etna.model;

import com.quest.etna.enums.UserRole;

public class UserDetails {
    private String username;
    private UserRole role;

    // Constructors
    public UserDetails(String username, UserRole role) {
        this.username = username;
        this.role = role;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public UserRole getRole() {
        return role;
    }

    // Setters

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}