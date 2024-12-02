package com.example.project.UnitTests;

import com.example.project.Backend.Dtos.CreatePlaylistDto;
import com.example.project.Backend.Models.Playlist;
import com.example.project.Backend.Models.User;
import com.example.project.Backend.Repositories.PlaylistRepository;
import com.example.project.Backend.Repositories.SongRepository;
import com.example.project.Backend.Repositories.UserRepository;
import com.example.project.Backend.Services.PlaylistService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class PlaylistServiceTest {
    @Mock
    private PlaylistRepository playlistRepository;

    @Mock
    private SongRepository songRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PlaylistService playlistService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("All playlists retrieval")
    void shouldGetAllPlaylists() {
        playlistService.getPlaylists();

        verify(playlistRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Playlist creation")
    void shouldCreatePlaylist() {
        int userId = 1;
        CreatePlaylistDto createPlaylistDto = new CreatePlaylistDto();
        createPlaylistDto.setName("Test Playlist");
        createPlaylistDto.setSongIds(Collections.emptyList());

        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));
        when(playlistRepository.existsByNameAndUser(anyString(), any(User.class))).thenReturn(false);

        playlistService.createPlaylist(userId, createPlaylistDto);

        verify(playlistRepository, times(1)).save(any(Playlist.class));
    }

    @Test
    @DisplayName("Playlist creation with unknown user")
    void shouldNotCreatePlaylistWithUnknownUser() {
        int userId = 1;
        CreatePlaylistDto createPlaylistDto = new CreatePlaylistDto();
        createPlaylistDto.setName("Test Playlist");
        createPlaylistDto.setSongIds(Collections.emptyList());

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.empty());

        assertThrows(RuntimeException.class, () -> playlistService.createPlaylist(userId, createPlaylistDto));
    }

    @Test
    @DisplayName("Playlist creation with existing playlist name")
    void shouldNotCreatePlaylistWithExistingName() {
        int userId = 1;
        CreatePlaylistDto createPlaylistDto = new CreatePlaylistDto();
        createPlaylistDto.setName("Test Playlist");
        createPlaylistDto.setSongIds(Collections.emptyList());

        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));
        when(playlistRepository.existsByNameAndUser(anyString(), any(User.class))).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> playlistService.createPlaylist(userId, createPlaylistDto));
    }

    @Test
    @DisplayName("Addition of song to non-existent playlist")
    void shouldNotAddSongToNonExistentPlaylist() {
        int playlistId = 1;
        int songId = 1;

        when(playlistRepository.existsById(playlistId)).thenReturn(false);
        when(songRepository.existsById(songId)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> playlistService.addSongToPlaylist(playlistId, songId));
    }

    @Test
    @DisplayName("Deletion of non-existent playlist")
    void shouldNotDeleteNonExistentPlaylist() {
        int playlistId = 1;

        when(playlistRepository.findById(playlistId)).thenReturn(java.util.Optional.empty());

        assertThrows(RuntimeException.class, () -> playlistService.deletePlaylist(playlistId));
    }
}
