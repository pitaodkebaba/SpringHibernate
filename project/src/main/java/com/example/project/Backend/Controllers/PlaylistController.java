package com.example.project.Backend.Controllers;

import com.example.project.Backend.Dtos.CreatePlaylistDto;
import com.example.project.Backend.Models.Playlist;
import com.example.project.Backend.Models.User;
import com.example.project.Backend.Responses.PlaylistResponse;
import com.example.project.Backend.Responses.SuccessResponse;
import com.example.project.Backend.Services.PlaylistService;
import com.example.project.Backend.Services.UserService;
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
    private final UserService userService;

    @GetMapping
    public ResponseEntity<SuccessResponse<List<? extends PlaylistResponse>>> getPlaylists() {
        List<? extends PlaylistResponse> playlists = playlistService.getPlaylists();
        SuccessResponse<List<? extends PlaylistResponse>> response = new SuccessResponse<>("Success", "Playlists retrieved successfully", playlists);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<SuccessResponse<List<Playlist>>> getMyPlaylists(){
        List<Playlist> playlists = playlistService.getPlaylistsByCurrentUser();
        SuccessResponse<List<Playlist>> response = new SuccessResponse<>("Success", "Your retrieved successfully", playlists);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPlaylistById(@PathVariable int id) throws AccessDeniedException {
        Optional<Playlist> playlist = playlistService.getPlaylistById(id);
        SuccessResponse<Optional<Playlist>> response = new SuccessResponse<>("Success", "Playlist retrieved successfully", playlist);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> createPlaylist(@Valid @RequestBody CreatePlaylistDto playlist) throws AccessDeniedException {
        User user = userService.authencticatedUser();
        playlistService.createPlaylist(user.getId(), playlist);
        SuccessResponse<Void> response = new SuccessResponse<>("Success", "Playlist created successfully", null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{playlistId}/addSong/{songId}")
    public ResponseEntity<?> addSongToPlaylist(@PathVariable int playlistId, @PathVariable int songId) {
        playlistService.addSongToPlaylist(playlistId, songId);
        SuccessResponse<Void> response = new SuccessResponse<>("Success", "Song added to playlist successfully", null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlaylist(@PathVariable int id) throws AccessDeniedException {
        playlistService.deletePlaylist(id);
        SuccessResponse<Void> response = new SuccessResponse<>("Success", "Playlist deleted successfully", null);
        return ResponseEntity.ok(response);
    }
}
