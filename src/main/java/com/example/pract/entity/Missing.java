package com.example.pract.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Missing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String first_name, last_name, loc_last_seen, other_info;

    private Double reward;

    private LocalDateTime missing_since;

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
        return loc_last_seen;
    }
    public void setLocation(String location) {
        this.loc_last_seen = location;
    }

    public String getOtherInfo() {
        return other_info;
    }
    public void setOtherInfo(String otherInfo) {
        this.other_info = otherInfo;
    }

    public Double getReward() {
        return reward;
    }
    public void setReward(Double reward) {
        this.reward = reward;
    }

    public LocalDateTime getMissingSinceDate() {
        return missing_since;
    }
    public void setMissingSinceDate(LocalDateTime missingSince) {
        this.missing_since = missingSince;
    }
}
