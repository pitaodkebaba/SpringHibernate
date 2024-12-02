package com.example.project.Backend.Controllers;

import com.example.project.Backend.Dtos.CreateSongDto;
import com.example.project.Backend.Models.Song;
import com.example.project.Backend.Responses.SuccessResponse;
import com.example.project.Backend.Services.SongService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
@AllArgsConstructor
public class SongController {
    private final SongService songService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<SuccessResponse<Void>> createSong(@Valid @RequestBody CreateSongDto song) {
        songService.createSong(song);
        SuccessResponse<Void> response = new SuccessResponse<>("Success", "Song created successfully", null);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/delete/{id}")
    public ResponseEntity<SuccessResponse<Void>> deleteSong(@PathVariable int id) {
        songService.deleteSong(id);
        SuccessResponse<Void> response = new SuccessResponse<>("Success", "Song deleted successfully", null);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<Song>>> getAllSongs() {
        List<Song> songs = songService.getAllSongs();
        SuccessResponse<List<Song>> response = new SuccessResponse<>("Success", "Songs retrieved successfully", songs);
        return ResponseEntity.ok(response);
    }

}
