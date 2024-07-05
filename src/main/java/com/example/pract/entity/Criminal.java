package com.example.pract.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Criminal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String first_name, last_name, location, crime;

    private Double reward;

    private LocalDateTime wanted_since;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return first_name;
    }
    public void setFirstName(String firstName) {
        this.first_name = firstName;
    }

    public String getLastName() {
        return last_name;
    }
    public void setLastName(String lastName) {
        this.last_name = lastName;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getCrime() {
        return crime;
    }
    public void setCrime(String crime) {
        this.crime = crime;
    }

    public Double getReward() {
        return reward;
    }
    public void setReward(Double reward) {
        this.reward = reward;
    }

    public LocalDateTime getWantedSinceDate() {
        return wanted_since;
    }
    public void setWantedSinceDate(LocalDateTime wantedSince) {
        this.wanted_since = wantedSince;
    }
}
