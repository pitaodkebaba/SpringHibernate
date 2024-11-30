package com.example.project.Dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateSongDto {
    private int id;
    @NotBlank(message = "Title is mandatory")
    private String title;
    @NotBlank(message = "Artist is mandatory")
    private String artist;
    @NotBlank(message = "Album is mandatory")
    private String album;

    private int genreId;
}
