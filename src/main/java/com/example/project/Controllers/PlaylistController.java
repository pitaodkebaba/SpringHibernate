package com.example.project.Controllers;

import com.example.project.Dtos.PlaylistDto;
import com.example.project.Models.Playlist;
import com.example.project.Repositories.PlaylistRepository;
import com.example.project.Services.PlaylistService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/playlists")
@AllArgsConstructor
public class PlaylistController {
    private final PlaylistService playlistService;

    @GetMapping
    public List<PlaylistDto> getPlaylists() {
        return playlistService.getPlaylists();
    }

    @GetMapping("/{id}")
    public Optional<PlaylistDto> getPlaylistById(@PathVariable Long id) {
        return playlistService.getPlaylistById(id);
    }

    @PostMapping
    public void createPlaylist(@RequestBody PlaylistDto playlistDto) {
        playlistService.createPlaylist(playlistDto);
    }

    @DeleteMapping("/{id}")
    public void deletePlaylist(@PathVariable Long id) {
        playlistService.deletePlaylist(id);
    }
}
