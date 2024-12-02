package com.example.project.UnitTests;

import com.example.project.Backend.Dtos.CreateSongDto;
import com.example.project.Backend.Models.Genre;
import com.example.project.Backend.Models.Song;
import com.example.project.Backend.Repositories.GenreRepository;
import com.example.project.Backend.Repositories.SongRepository;
import com.example.project.Backend.Services.SongService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class SongServiceTest {

    @Mock
    private SongRepository songRepository;

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private SongService songService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("All songs retrieval")
    void shouldGetAllSongs() {
        songService.getAllSongs();

        verify(songRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Song retrieval by id")
    void shouldGetSongById() {
        int songId = 1;

        when(songRepository.findById(songId)).thenReturn(Optional.of(new Song()));

        songService.getSongById(songId);

        verify(songRepository, times(1)).findById(songId);
    }

    @Test
    @DisplayName("Song creation")
    void shouldCreateSong() {
        CreateSongDto createSongDto = new CreateSongDto();
        createSongDto.setTitle("Test Song");
        createSongDto.setArtist("Test Artist");
        createSongDto.setAlbum("Test Album");
        createSongDto.setGenreId(1);

        when(songRepository.existsByTitleAndAlbum(anyString(), anyString())).thenReturn(false);
        when(genreRepository.findById(anyInt())).thenReturn(Optional.of(new Genre()));

        songService.createSong(createSongDto);

        verify(songRepository, times(1)).save(any(Song.class));
    }

    @Test
    @DisplayName("Song creation with existing song title and album")
    void shouldNotCreateSongWithExistingTitleAndAlbum() {
        CreateSongDto createSongDto = new CreateSongDto();
        createSongDto.setTitle("Test Song");
        createSongDto.setArtist("Test Artist");
        createSongDto.setAlbum("Test Album");
        createSongDto.setGenreId(1);

        when(songRepository.existsByTitleAndAlbum(anyString(), anyString())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> songService.createSong(createSongDto));
    }

    @Test
    @DisplayName("Song deletion")
    void shouldDeleteSong() {
        int songId = 1;

        when(songRepository.existsById(songId)).thenReturn(true);
        when(songRepository.findById(songId)).thenReturn(Optional.of(new Song()));

        songService.deleteSong(songId);

        verify(songRepository, times(1)).delete(any(Song.class));
    }

    @Test
    @DisplayName("Song deletion with non-existent song")
    void shouldNotDeleteNonExistentSong() {
        int songId = 1;

        when(songRepository.existsById(songId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> songService.deleteSong(songId));
    }
}
