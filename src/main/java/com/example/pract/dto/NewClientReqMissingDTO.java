package com.example.pract.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

public class NewClientReqMissingDTO {
    @Valid

    @NotEmpty(message = "Missing person's firstname can not be empty")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]{2,30}$", message = "Firstname must be between 2 and 30 characters and contain only letters")
    private String missFirstName;

    @NotEmpty(message = "Missing person's lastname can not be empty")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]{2,30}$", message = "Lastname must be between 2 and 30 characters and contain only letters")
    private String missLastName;

    @NotEmpty(message = "Last seen location can not be empty")
    @Pattern(regexp = "^.{2,70}$", message = "Location must be between 2 and 70 characters")
    private String locLastSeen;

    @DateTimeFormat(pattern = "yyyy-MM-dd' 'HH:mm")
    @NotNull(message = "Missing since date can not be empty")
    private LocalDateTime missingSince;

    @Pattern(regexp = "^.{0,200}$", message = "Other info must be no longer than 200 characters")
    private String otherInfo;

    @Min(value = 1/100, message = "Reward must be greater than 0")
    private Double reward;

    public String getMissFirstName() { return missFirstName; }
    public void setMissFirstName(String missFirstName) { this.missFirstName = missFirstName; }

    public String getMissLastName() { return missLastName; }
    public void setMissLastName(String missLastName) { this.missLastName = missLastName; }

    public String getLocLastSeen() { return locLastSeen; }
    public void setLocLastSeen(String locLastSeen) { this.locLastSeen = locLastSeen; }

    public LocalDateTime getMissingSince() {
        return missingSince;
    }
    public void setMissingSince(LocalDateTime missingSince) {
        this.missingSince = missingSince;
    }

    public String getOtherInfo() { return otherInfo; }
    public void setOtherInfo(String otherInfo) { this.otherInfo = otherInfo; }

    public Double getReward() {
        return reward;
    }
    public void setReward(Double reward) {
        this.reward = reward;
    }
}
