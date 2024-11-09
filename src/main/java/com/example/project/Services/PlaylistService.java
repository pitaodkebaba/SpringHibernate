package com.example.project.Services;


import com.example.project.Dtos.PlaylistDto;
import com.example.project.Models.Playlist;
import com.example.project.Models.User;
import com.example.project.Repositories.PlaylistRepository;
import com.example.project.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PlaylistService {
    public final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;

    public List<PlaylistDto> getPlaylists() {
        return playlistRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<PlaylistDto> getPlaylistById(Long id) {
        return playlistRepository.findById(id)
                .map(this::convertToDto);
    }

    public PlaylistDto createPlaylist(PlaylistDto playlistDto){
        Playlist playlist = convertToEntity(playlistDto);
        Playlist savedPlaylist = playlistRepository.save(playlist);
        return convertToDto(savedPlaylist);
    }

    public void deletePlaylist(Long id){
        playlistRepository.deleteById(id);
    }

    //Konwersja dto
    private PlaylistDto convertToDto(Playlist playlist) {
        PlaylistDto playlistDto = new PlaylistDto();
        playlistDto.setId(playlist.getId());
        playlistDto.setName(playlist.getName());
        playlistDto.setUserId(playlist.getUser().getId());
        return playlistDto;
    }

    private Playlist convertToEntity(PlaylistDto playlistDto){
        Playlist playlist = new Playlist();
        playlist.setName(playlistDto.getName());
        playlist.setId(playlistDto.getId());

        User user = userRepository.findById(playlistDto.getUserId()).orElseThrow();
        playlist.setUser(user);

        return playlist;
    }
}
