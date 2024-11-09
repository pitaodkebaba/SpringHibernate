package com.example.project.Dtos;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class PlaylistDto {
    @NotNull
    private Long id;
    @NotNull(message = "Name is required")
    private String name;
    @NotNull
    private Long userId;
}
