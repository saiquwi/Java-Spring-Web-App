package com.example.pract.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class NewCriminalDTO {
    @Valid

    @NotEmpty(message = "Criminal's firstname can not be empty")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]{2,30}$", message = "Firstname must be between 2 and 30 characters and contain only letters")
    private String criminalFirstName;

    @NotEmpty(message = "Criminal's lastname can not be empty")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]{2,30}$", message = "Lastname must be between 2 and 30 characters and contain only letters")
    private String criminalLastName;

    @NotEmpty(message = "Location can not be empty")
    @Pattern(regexp = "^.{2,70}$", message = "Location must be between 2 and 70 characters")
    private String location;

    @NotEmpty(message = "Crime can not be empty")
    @Pattern(regexp = "^.{2,200}$", message = "Crime must be between 2 and 200 characters")
    private String crime;

    @DateTimeFormat(pattern = "yyyy-MM-dd' 'HH:mm")
    @NotNull(message = "Wanted since date can not be empty")
    private LocalDateTime wantedSince;

    @Min(value = 1/100, message = "Reward must be greater than 0")
    private Double reward;

    public String getCriminalFirstName() { return criminalFirstName; }
    public void setCriminalFirstName(String criminalFirstName) { this.criminalFirstName = criminalFirstName; }

    public String getCriminalLastName() { return criminalLastName; }
    public void setCriminalLastName(String criminalLastName) { this.criminalLastName = criminalLastName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getCrime() { return crime; }
    public void setCrime(String crime) { this.crime = crime; }

    public LocalDateTime getWantedSince() {
        return wantedSince;
    }
    public void setWantedSince(LocalDateTime wantedSince) {
        this.wantedSince = wantedSince;
    }

    public Double getReward() {
        return reward;
    }
    public void setReward(Double reward) {
        this.reward = reward;
    }
}
