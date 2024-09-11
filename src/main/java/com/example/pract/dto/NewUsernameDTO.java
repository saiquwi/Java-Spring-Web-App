package com.example.pract.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

@Validated
public class NewUsernameDTO {
    @NotBlank(message = "Username can not be empty")
    @Pattern(regexp = "^[a-zA-Z0-9]{3,30}$", message = "Username must be between 3 and 30 characters and contain only letters and digits")
    private String username;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
