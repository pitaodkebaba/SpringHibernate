package com.example.project.Services;

import com.example.project.Dtos.SongDto;
import com.example.project.Models.Song;
import com.example.project.Repositories.SongRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SongService {
    private final SongRepository songRepository;

    public List<SongDto> getAllSongs() {
        List<Song> songs = songRepository.findAll();
        return songs.stream().map(this::convertToDto).toList();
    }

    public Optional<Song> getSongById(int id) {
        return songRepository.findById(id);
    }

    public void createSong(Song song) {
        songRepository.save(song);
    }

    public void deleteSong(int id) {
        songRepository.delete(songRepository.findById(id).orElseThrow());
    }

    public SongDto convertToDto(Song song) {
        SongDto songDto = new SongDto();
        songDto.setId(song.getId());
        songDto.setTitle(song.getTitle());
        songDto.setArtist(song.getArtist());
        return songDto;
    }

    public Song convertToEntity(SongDto songDto) {
        Song song = new Song();
        song.setId(songDto.getId());
        song.setTitle(songDto.getTitle());
        song.setArtist(songDto.getArtist());
        return song;
    }
}
