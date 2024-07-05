package com.example.pract.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class NewUserRequestDTO {
    @Valid

    @NotEmpty(message = "Username can not be empty")
    @Pattern(regexp = "^[a-zA-Z0-9]{3,30}$", message = "Username must be between 3 and 30 characters and contain only letters and digits")
    private String username;

    @NotEmpty(message = "Firstname can not be empty")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]{2,30}$", message = "Firstname must be between 2 and 30 characters and contain only letters and digits")
    private String firstName;

    @NotEmpty(message = "Lastname can not be empty")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]{2,30}$", message = "Lastname must be between 2 and 30 characters and contain only letters and digits")
    private String lastName;

    @NotEmpty(message = "Password can not be empty")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,40}$",
            message = "Password must be between 8 and 40 characters long, and contain at least one lowercase letter, one uppercase letter, one digit and one special character from the set @$!%*?&."
    )
    private String password; //пример пароля aaAA11!!

    @NotEmpty(message = "Please confirm your password")
    private String confirmPassword;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}
