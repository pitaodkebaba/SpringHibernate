package com.example.project.Services;

import com.example.project.Dtos.CreateSongDto;
import com.example.project.Models.Song;
import com.example.project.Repositories.GenreRepository;
import com.example.project.Repositories.SongRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SongService {
    private final SongRepository songRepository;
    private final GenreRepository genreRepository;

    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    public Optional<Song> getSongById(int id) {
        return songRepository.findById(id);
    }

    @Transactional
    public void createSong(CreateSongDto song) {
        if (songRepository.existsByTitleAndAlbum(song.getTitle(), song.getAlbum())) {
            throw new IllegalArgumentException("Song already exists on that album");
        }
        songRepository.save(convertToEntity(song));
    }

    public void deleteSong(int id) {
        if (!songRepository.existsById(id)) {
            throw new IllegalArgumentException("Song with id: " + id + " not found");
        }
        songRepository.delete(songRepository.findById(id).orElseThrow());
    }

    public CreateSongDto convertToDto(Song song) {
        CreateSongDto createSongDto = new CreateSongDto();
        createSongDto.setTitle(song.getTitle());
        createSongDto.setArtist(song.getArtist());
        createSongDto.setAlbum(song.getAlbum());
        createSongDto.setGenreId(song.getGenre().getId());
        return createSongDto;
    }

    public Song convertToEntity(CreateSongDto createSongDto) {
        Song song = new Song();
        song.setTitle(createSongDto.getTitle());
        song.setArtist(createSongDto.getArtist());
        song.setAlbum(createSongDto.getAlbum());
        song.setGenre(genreRepository.findById(createSongDto.getGenreId()).orElseThrow());
        return song;
    }
}
