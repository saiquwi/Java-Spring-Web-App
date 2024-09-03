package com.example.pract.entity;

import com.example.pract.enums.ReqStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

@Entity
public class ClientRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private User user;
    private String miss_first_name, miss_last_name, loc_last_seen, other_info;

    private Double reward;

    @Enumerated(EnumType.STRING) //REQUESTED, ACCEPTED, DENIED, FOUND, DEAD, NO_TRACE
    private ReqStatus status;

    private LocalDateTime missing_since, req_date;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public String getFirstName() {
        return miss_first_name;
    }
    public void setFirstName(String firstName) {
        this.miss_first_name = firstName;
    }

    public String getLastName() {
        return miss_last_name;
    }
    public void setLastName(String lastName) {
        this.miss_last_name = lastName;
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

    public ReqStatus getStatus() { return status; }
    public void setStatus(ReqStatus status) { this.status = status; }

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

    public LocalDateTime getReqDate() {
        return req_date;
    }
    public void setReqDate(LocalDateTime reqDate) {
        this.req_date = reqDate;
    }
}
