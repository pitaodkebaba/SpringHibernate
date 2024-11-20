package com.example.project.Dtos;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class PlaylistDto {
    @NotNull
    private int id;
    @NotNull(message = "Name is required")
    private String name;
    @NotNull
    private String owner;
    @NotNull
    private List<String> songs;
}
