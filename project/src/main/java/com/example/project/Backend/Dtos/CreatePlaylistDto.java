package com.example.project.Backend.Dtos;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class CreatePlaylistDto {
    @NotBlank(message = "Playlist name is mandatory")
    private String name;
    //Playlist may be added without songs
    private List<Integer> songIds;
}
