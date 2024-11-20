package com.example.project.Controllers;

import com.example.project.Dtos.SongDto;
import com.example.project.Models.Song;
import com.example.project.Services.SongService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
@AllArgsConstructor
public class SongController {
    private final SongService songService;

    @PostMapping
    public void createSong(@RequestBody Song song) {
        songService.createSong(song);
    }

    @PostMapping("/delete/{id}")
    public void deleteSong(@RequestBody int id) {
        songService.deleteSong(id);
    }

    @GetMapping
    public List<SongDto> getAllSongs() {
        return songService.getAllSongs();
    }

}
