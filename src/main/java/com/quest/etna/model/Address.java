package com.quest.etna.model;

import java.time.LocalDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;

@Entity
public class Address {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Basic
    @Column(name = "street", nullable = false, length = 100)
    private String street;

    @Basic
    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @Basic
    @Column(name = "postalCode", nullable = false, length = 10)
    private String postalCode;

    @Basic
    @Column(name = "country", nullable = false, length = 50)
    private String country;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user", nullable = false)
    private User user;

    @Basic
    @Column(name = "creationAt", nullable = false)
    private LocalDateTime creationAt;

    @Basic
    @Column(name = "UpdateAt", nullable = true)
    private LocalDateTime updateAt;

    // Constructors
    public Address() {
    }

    public Address(String street, String city, String postalCode, String country) {
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }

    @PrePersist
    public void onCreate() {
        if (creationAt == null) {
            creationAt = LocalDateTime.now();

        }
    }
    // Getters

    public long getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getCreationAt() {
        return creationAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    // Setters

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCreationAt(LocalDateTime creationAt) {
        this.creationAt = creationAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Address adress = (Address) o;

        return Objects.equals(street, adress.street)
                && Objects.equals(city, adress.city)
                && Objects.equals(postalCode, adress.postalCode)
                && Objects.equals(country, adress.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, city, postalCode, country);
    }

    @Override
    public String toString() {
        return "Adress{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", country='" + country + '\'' +
                ", user=" + user +
                ", creationAt=" + creationAt +
                ", updateAt=" + updateAt +
                '}';
    }
}
