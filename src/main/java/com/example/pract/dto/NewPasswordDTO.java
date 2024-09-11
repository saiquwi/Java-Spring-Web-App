package com.example.pract.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class NewPasswordDTO {

    @Valid

    @NotEmpty(message = "Password can not be empty")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,40}$",
            message = "Password must be between 8 and 40 characters long, and contain at least one lowercase letter, one uppercase letter, one digit and one special character from the set @$!%*?&."
    )
    private String password; //пример пароля aaAA11!!

    @NotEmpty(message = "Please confirm your password")
    private String confirmPassword;

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}
