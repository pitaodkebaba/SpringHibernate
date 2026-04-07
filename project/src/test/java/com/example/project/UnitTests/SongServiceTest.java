package com.example.project.UnitTests;

import com.example.project.Backend.Dtos.CreateSongDto;
import com.example.project.Backend.Models.Genre;
import com.example.project.Backend.Models.Song;
import com.example.project.Backend.Models.User;
import com.example.project.Backend.Repositories.GenreRepository;
import com.example.project.Backend.Repositories.SongRepository;
import com.example.project.Backend.Responses.AdminSongResponse;
import com.example.project.Backend.Responses.SongResponse;
import com.example.project.Backend.Services.SongService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class SongServiceTest {

    @Mock
    private SongRepository songRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext s;

    @InjectMocks
    private SongService songService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(s);
    }

    @Test
    @DisplayName("All songs retrieval for admin")
    void shouldGetAllSongs() {
        User admin = new User();
        admin.setRole(User.Role.ADMIN);

        when(s.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(admin);

        List<Song> songs = List.of(new Song(), new Song());
        songs.get(0).setGenre(new Genre());
        songs.get(1).setGenre(new Genre());
        when(songRepository.findAll()).thenReturn(songs);

        List<? extends SongResponse> result = songService.getAllSongs();

        assertEquals(2, result.size());
        assertEquals(AdminSongResponse.class, result.get(0).getClass());
        verify(songRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("All songs retrieval for user")
    void shouldGetAllSongsForUser() {
        User user = new User();
        user.setRole(User.Role.USER);

        when(s.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);

        List<Song> songs = List.of(new Song(), new Song());
        songs.get(0).setGenre(new Genre());
        songs.get(1).setGenre(new Genre());
        when(songRepository.findAll()).thenReturn(songs);

        List<? extends SongResponse> result = songService.getAllSongs();

        assertEquals(2, result.size());
        assertEquals(SongResponse.class, result.get(0).getClass());
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
