package com.example.project.Controllers;

import com.example.project.Dtos.CreatePlaylistDto;
import com.example.project.Models.User;
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
    private final UserService userService;

    @GetMapping
    public ResponseEntity<SuccessResponse<List<GetPlaylistResponse>>> getPlaylists() {
        List<GetPlaylistResponse> playlists = playlistService.getPlaylists();
        SuccessResponse<List<GetPlaylistResponse>> response = new SuccessResponse<>("Success", "Playlists retrieved successfully", playlists);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPlaylistById(@PathVariable int id) {
        Optional<GetPlaylistResponse> playlist = playlistService.getPlaylistById(id);
        SuccessResponse<Optional<GetPlaylistResponse>> response = new SuccessResponse<>("Success", "Playlist retrieved successfully", playlist);
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
    public ResponseEntity<?> deletePlaylist(@PathVariable int id) {
        playlistService.deletePlaylist(id);
        SuccessResponse<Void> response = new SuccessResponse<>("Success", "Playlist deleted successfully", null);
        return ResponseEntity.ok(response);
    }
}
