package com.example.project.Backend.Dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateSongDto {
    @NotBlank(message = "Song title is mandatory")
    private String title;
    @NotBlank(message = "Artist is mandatory")
    private String artist;
    @NotBlank(message = "Album is mandatory")
    private String album;
    @NotNull(message = "Genre is mandatory")
    private int genreId;
}
