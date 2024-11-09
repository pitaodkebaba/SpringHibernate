package com.example.project.Dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserDto {
    @NotNull(message = "Name is required")
    private String username;
    @NotNull(message = "Password is required")
    private String password;
}
