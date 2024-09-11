package com.example.pract.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class NewProfileDTO {

    @Valid

    @NotEmpty(message = "Firstname can not be empty")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]{2,30}$", message = "Firstname must be between 2 and 30 characters and contain only letters and digits")
    private String firstName;

    @NotEmpty(message = "Lastname can not be empty")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]{2,30}$", message = "Lastname must be between 2 and 30 characters and contain only letters and digits")
    private String lastName;

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
}
