package com.example.project.Models;

import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Playlist {
    private Long id;
    private String name;
    private User user;

    @ManyToMany
    private List<Song> songs;
}
