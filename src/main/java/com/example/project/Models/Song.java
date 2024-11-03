package com.example.project.Models;


import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Song {
    private Long id;
    private String title;
    private String artist;
    private String album;
    private String genre;

    @ManyToMany(mappedBy = "songs")
    private List<Playlist> playlists;
}
