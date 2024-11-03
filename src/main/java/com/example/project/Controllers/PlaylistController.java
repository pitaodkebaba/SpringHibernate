package com.example.project.Controllers;

import com.example.project.Models.Playlist;
import com.example.project.Repositories.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {
    @Autowired
    private PlaylistRepository playlistRepository;

    @GetMapping
    public List<Playlist> getAll() {
        return playlistRepository.findAll();
    }

    @PostMapping
    public Playlist createPlaylist(Playlist playlist) {
        return playlistRepository.save(playlist);
    }

    @DeleteMapping("/{id}")
    public void deletePlaylist(@PathVariable Long id) {
        playlistRepository.deleteById(id);
    }
}
