package com.example.project.Dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateSongDto {
    @NotBlank(message = "Song title is mandatory")
    private String title;
    @NotBlank(message = "Artist is mandatory")
    private String artist;
    @NotBlank(message = "Album is mandatory")
    private String album;
    @NotBlank(message = "Genre Id is mandatory")
    private int genreId;
}
