package com.example.project.Controllers;

import com.example.project.Dtos.CreatePlaylistDto;
import com.example.project.Models.Playlist;
import com.example.project.Models.User;
import com.example.project.Repositories.PlaylistRepository;
import com.example.project.Repositories.UserRepository;
import com.example.project.Responses.GetPlaylistResponse;
import com.example.project.Responses.SuccessResponse;
import com.example.project.Services.PlaylistService;
import com.example.project.Services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/playlists")
@AllArgsConstructor
public class PlaylistController {
    private final PlaylistService playlistService;
    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<Playlist>> getPlaylists() {
        return ResponseEntity.ok(playlistService.getPlaylists());
    }

    @GetMapping("/{id}")
    public Optional<GetPlaylistResponse> getPlaylistById(@PathVariable int id) {
        return playlistService.getPlaylistById(id);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<Void>> createPlaylist(@Valid @RequestBody CreatePlaylistDto playlist) throws AccessDeniedException {
        User user = userService.authencticatedUser();
        playlistService.createPlaylist(user.getId(), playlist);
        SuccessResponse<Void> response = new SuccessResponse<>("Success", "Playlist created successfully", null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{playlistId}/addSong/{songId}")
    public ResponseEntity<SuccessResponse<Void>> addSongToPlaylist(@PathVariable int playlistId, @PathVariable int songId) {
        playlistService.addSongToPlaylist(playlistId, songId);
        SuccessResponse<Void> response = new SuccessResponse<>(
                "Success", "Song added to playlist " + playlistRepository
                .findById(playlistId)
                .map(Playlist::getName)
                .orElse(String.valueOf(playlistId)) + " successfully", null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Void>> deletePlaylist(@PathVariable int id) {
        playlistService.deletePlaylist(id);
        SuccessResponse<Void> response = new SuccessResponse<>("Success", "Playlist deleted successfully", null);
        return ResponseEntity.ok(response);
    }
}
