package com.example.pract.dto;

import com.example.pract.enums.NewsType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class NewsDTO {

    @Valid

    @NotEmpty(message = "Title can not be empty")
    @Pattern(regexp = "^.{3,300}$", message = "Title must be between 3 and 300 characters")
    private String title;

    @NotEmpty(message = "Description can not be empty")
    @Pattern(regexp = "^.{10,}$", message = "Description must be at least 10 characters")
    private String description;

    @NotNull(message = "News type must be selected")
    private NewsType type;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public NewsType getType() { return type; }
    public void setType(NewsType type) { this.type = type; }
}
